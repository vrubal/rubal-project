package com.rubal.caching.resilient;

import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;

import java.util.List;
import java.util.concurrent.Callable;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Unit tests for {@link ResilientCacheManager} covering cache wrapping, name delegation, the
 * {@link RedisCacheOps} requirement on delegate caches, and the recovery wipe of the fallback store.
 */
@DisplayName("ResilientCacheManager")
class ResilientCacheManagerTest {

    private CircuitBreakerRegistry registry;
    private FallbackStore fallbackStore;

    @BeforeEach
    void setUp() {
        registry = CircuitBreakerRegistry.ofDefaults();
        fallbackStore = new FallbackStore(100, 0);
    }

    private ResilientCacheManager newManager(CacheManager delegate) {
        return new ResilientCacheManager(delegate, fallbackStore, registry);
    }

    @Test
    @DisplayName("wraps a RedisCacheOps-capable delegate cache in a ResilientCache")
    void wrapsDelegateCache() {
        CacheManager delegate = mock(CacheManager.class);
        when(delegate.getCache("c1")).thenReturn(new FakeRedisCache("c1"));

        ResilientCacheManager manager = newManager(delegate);
        Cache cache = manager.getCache("c1");

        assertThat(cache).isInstanceOf(ResilientCache.class);
        assertThat(cache.getName()).isEqualTo("c1");
    }

    @Test
    @DisplayName("returns the same ResilientCache instance for repeated lookups")
    void cachesResilientInstances() {
        CacheManager delegate = mock(CacheManager.class);
        when(delegate.getCache("c1")).thenReturn(new FakeRedisCache("c1"));

        ResilientCacheManager manager = newManager(delegate);

        assertThat(manager.getCache("c1")).isSameAs(manager.getCache("c1"));
    }

    @Test
    @DisplayName("delegates getCacheNames to the underlying manager")
    void delegatesCacheNames() {
        CacheManager delegate = mock(CacheManager.class);
        when(delegate.getCacheNames()).thenReturn(List.of("a", "b"));

        ResilientCacheManager manager = newManager(delegate);

        assertThat(manager.getCacheNames()).containsExactlyInAnyOrder("a", "b");
    }

    @Test
    @DisplayName("throws when delegate cache does not implement RedisCacheOps")
    void rejectsNonRedisCacheOpsDelegate() {
        CacheManager delegate = mock(CacheManager.class);
        Cache plainCache = mock(Cache.class); // not a RedisCacheOps
        when(delegate.getCache("c1")).thenReturn(plainCache);

        ResilientCacheManager manager = newManager(delegate);

        assertThatThrownBy(() -> manager.getCache("c1"))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("RedisCacheOps");
    }

    @Test
    @DisplayName("throws when delegate returns no cache for a name")
    void rejectsMissingDelegateCache() {
        CacheManager delegate = mock(CacheManager.class);
        when(delegate.getCache("missing")).thenReturn(null);

        ResilientCacheManager manager = newManager(delegate);

        assertThatThrownBy(() -> manager.getCache("missing"))
                .isInstanceOf(IllegalStateException.class);
    }

    @Test
    @DisplayName("wipes the fallback store when the circuit transitions back to CLOSED")
    void recoveryWipesFallback() {
        newManager(mock(CacheManager.class)); // registers the recovery listener
        fallbackStore.put("c1::k1", "v1");
        assertThat(fallbackStore.estimatedSize()).isEqualTo(1L);

        CircuitBreaker breaker = registry.circuitBreaker(ResilientCacheManager.CIRCUIT_BREAKER_NAME);
        breaker.transitionToOpenState();
        breaker.transitionToHalfOpenState();
        breaker.transitionToClosedState();

        assertThat(fallbackStore.estimatedSize()).isZero();
    }

    /**
     * Minimal Spring {@link Cache} that also implements {@link RedisCacheOps}, mirroring how the real
     * {@code JedisCSCCache} satisfies both contracts.
     */
    private static final class FakeRedisCache implements Cache, RedisCacheOps {
        private final String name;

        private FakeRedisCache(String name) {
            this.name = name;
        }

        @Override
        public String getName() {
            return name;
        }

        @Override
        public Object getNativeCache() {
            return this;
        }

        @Override
        public ValueWrapper get(Object key) {
            return null;
        }

        @Override
        public <T> T get(Object key, Class<T> type) {
            return null;
        }

        @Override
        public <T> T get(Object key, Callable<T> valueLoader) {
            return null;
        }

        @Override
        public void put(Object key, Object value) {
            // no-op
        }

        @Override
        public void evict(Object key) {
            // no-op
        }

        @Override
        public void clear() {
            // no-op
        }

        @Override
        public String compositeKey(Object key) {
            return name + "::" + key;
        }
    }
}

