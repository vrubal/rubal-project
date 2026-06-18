package com.rubal.caching.resilient;

import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.Cache;

import java.util.concurrent.Callable;
import java.util.function.Supplier;

/**
 * Resilient {@link Cache} that drives Redis access through a Resilience4j {@link CircuitBreaker} and
 * transparently falls back to a shared in-memory {@link FallbackStore} when Redis is unavailable.
 * <p>
 * <strong>While the circuit is CLOSED</strong> every read and write goes to Redis and successful
 * results are mirrored into the fallback store so it stays warm. This guarantees any key set or read
 * through the service is available locally the instant Redis goes down.
 * <p>
 * <strong>While the circuit is OPEN</strong> (or a Redis call throws) the operation short-circuits to
 * the in-memory mirror, so the application keeps functioning with the most recently observed values.
 * Resilience4j automatically probes for recovery; when the circuit closes the mirror is wiped (by the
 * owning {@link ResilientCacheManager}) and traffic resumes against Redis.
 */
public class ResilientCache implements Cache {

    private static final Logger logger = LoggerFactory.getLogger(ResilientCache.class);

    private final String name;
    private final Cache redisCache;
    private final RedisCacheOps ops;
    private final CircuitBreaker circuitBreaker;
    private final FallbackStore fallbackStore;

    /**
     * @param name           the cache name
     * @param redisCache     the Redis-backed Spring cache (used for {@link #getNativeCache()})
     * @param ops            the same Redis cache exposed as raw operations
     * @param circuitBreaker the shared circuit breaker guarding Redis access
     * @param fallbackStore  the shared in-memory mirror
     */
    public ResilientCache(String name,
                          Cache redisCache,
                          RedisCacheOps ops,
                          CircuitBreaker circuitBreaker,
                          FallbackStore fallbackStore) {
        this.name = name;
        this.redisCache = redisCache;
        this.ops = ops;
        this.circuitBreaker = circuitBreaker;
        this.fallbackStore = fallbackStore;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Object getNativeCache() {
        return redisCache.getNativeCache();
    }

    @Override
    public ValueWrapper get(Object key) {
        return withFallback(
                () -> {
                    ValueWrapper wrapper = ops.get(key);
                    if (wrapper != null) {
                        fallbackStore.put(ops.compositeKey(key), wrapper.get());
                    }
                    return wrapper;
                },
                () -> fallbackStore.get(ops.compositeKey(key)));
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T get(Object key, Class<T> type) {
        ValueWrapper wrapper = get(key);
        if (wrapper == null) {
            return null;
        }
        Object value = wrapper.get();
        if (value != null && type != null && !type.isInstance(value)) {
            throw new IllegalStateException(
                    "Cached value is not of required type [" + type.getName() + "]: " + value);
        }
        return (T) value;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T get(Object key, Callable<T> valueLoader) {
        ValueWrapper wrapper = get(key);
        if (wrapper != null) {
            return (T) wrapper.get();
        }
        synchronized (this) {
            wrapper = get(key);
            if (wrapper != null) {
                return (T) wrapper.get();
            }
            T value;
            try {
                value = valueLoader.call();
            } catch (Exception e) {
                throw new ValueRetrievalException(key, valueLoader, e);
            }
            put(key, value);
            return value;
        }
    }

    @Override
    public void put(Object key, Object value) {
        // Always mirror so the fallback reflects every write, even if the Redis write later fails.
        fallbackStore.put(ops.compositeKey(key), value);
        withFallback(() -> {
            ops.put(key, value);
            return null;
        }, () -> null);
    }

    @Override
    public void evict(Object key) {
        fallbackStore.evict(ops.compositeKey(key));
        withFallback(() -> {
            ops.evict(key);
            return null;
        }, () -> null);
    }

    @Override
    public void clear() {
        fallbackStore.clear();
        withFallback(() -> {
            ops.clear();
            return null;
        }, () -> null);
    }

    /**
     * Runs a Redis call through the circuit breaker, serving the in-memory fallback if the circuit is
     * open or the call fails.
     */
    private <T> T withFallback(Supplier<T> redisCall, Supplier<T> fallback) {
        try {
            return circuitBreaker.executeSupplier(redisCall);
        } catch (RuntimeException ex) {
            logger.debug("Redis call short-circuited/failed for cache '{}', serving fallback: {}",
                    name, ex.toString());
            return fallback.get();
        }
    }
}

