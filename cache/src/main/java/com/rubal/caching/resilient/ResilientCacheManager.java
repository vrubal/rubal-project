package com.rubal.caching.resilient;

import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;

import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Resilient {@link CacheManager} that decorates a Redis-backed delegate so the application keeps
 * serving cached data when Redis is unavailable.
 * <p>
 * Every cache returned is a {@link ResilientCache} guarded by a single shared Resilience4j
 * {@link CircuitBreaker} and backed by one shared {@link FallbackStore}. The circuit breaker detects
 * failures, short-circuits to the in-memory mirror while Redis is down, and automatically probes for
 * recovery. When it transitions back to CLOSED the mirror is wiped so stale fallback data can never
 * be served once Redis is authoritative again.
 */
public class ResilientCacheManager implements CacheManager {

    /** Name of the Resilience4j circuit breaker guarding Redis access. */
    public static final String CIRCUIT_BREAKER_NAME = "redisCache";

    private static final Logger logger = LoggerFactory.getLogger(ResilientCacheManager.class);

    private final CacheManager delegate;
    private final FallbackStore fallbackStore;
    private final CircuitBreaker circuitBreaker;

    private final ConcurrentMap<String, ResilientCache> caches = new ConcurrentHashMap<>();

    /**
     * @param delegate               the Redis-backed cache manager to decorate (its caches must
     *                               implement {@link RedisCacheOps})
     * @param fallbackStore          the shared in-memory mirror
     * @param circuitBreakerRegistry the Resilience4j registry the shared breaker is resolved from
     */
    public ResilientCacheManager(CacheManager delegate,
                                 FallbackStore fallbackStore,
                                 CircuitBreakerRegistry circuitBreakerRegistry) {
        this.delegate = delegate;
        this.fallbackStore = fallbackStore;
        this.circuitBreaker = circuitBreakerRegistry.circuitBreaker(CIRCUIT_BREAKER_NAME);
        registerRecoveryListener();
    }

    @Override
    public Cache getCache(String name) {
        return caches.computeIfAbsent(name, this::createResilientCache);
    }

    @Override
    public Collection<String> getCacheNames() {
        return delegate.getCacheNames();
    }

    private ResilientCache createResilientCache(String name) {
        Cache redisCache = delegate.getCache(name);
        if (redisCache == null) {
            throw new IllegalStateException("Delegate cache manager returned no cache for name: " + name);
        }
        if (!(redisCache instanceof RedisCacheOps redisCacheOps)) {
            throw new IllegalStateException(
                    "Delegate cache '" + name + "' must implement RedisCacheOps to be made resilient, but was "
                            + redisCache.getClass().getName());
        }
        return new ResilientCache(name, redisCache, redisCacheOps, circuitBreaker, fallbackStore);
    }

    /**
     * Wipes the shared mirror whenever the circuit closes (Redis recovered) so stale fallback data is
     * never served once Redis is authoritative again.
     */
    private void registerRecoveryListener() {
        circuitBreaker.getEventPublisher().onStateTransition(event -> {
            CircuitBreaker.State to = event.getStateTransition().getToState();
            if (to == CircuitBreaker.State.CLOSED) {
                fallbackStore.clear();
                logger.info("Redis circuit '{}' CLOSED - cleared in-memory fallback after recovery.",
                        CIRCUIT_BREAKER_NAME);
            } else if (to == CircuitBreaker.State.OPEN) {
                logger.warn("Redis circuit '{}' OPEN - serving cache from in-memory fallback.",
                        CIRCUIT_BREAKER_NAME);
            }
        });
    }
}

