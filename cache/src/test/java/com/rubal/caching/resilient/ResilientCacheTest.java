package com.rubal.caching.resilient;

import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.cache.Cache;
import org.springframework.cache.Cache.ValueWrapper;
import org.springframework.cache.support.SimpleValueWrapper;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit tests for {@link ResilientCache} covering mirroring while Redis is healthy and serving from
 * the in-memory fallback when Redis fails or the circuit is open.
 */
@DisplayName("ResilientCache")
class ResilientCacheTest {

    private static final String CACHE = "myCache";

    private CircuitBreaker circuitBreaker;
    private FallbackStore fallbackStore;
    private StubRedisCacheOps redisOps;
    private ResilientCache cache;

    @BeforeEach
    void setUp() {
        CircuitBreakerRegistry registry = CircuitBreakerRegistry.ofDefaults();
        circuitBreaker = registry.circuitBreaker(ResilientCacheManager.CIRCUIT_BREAKER_NAME);
        fallbackStore = new FallbackStore(1000, 0);
        redisOps = new StubRedisCacheOps();
        cache = new ResilientCache(CACHE, redisOps, redisOps, circuitBreaker, fallbackStore);
    }

    @Test
    @DisplayName("get from healthy Redis mirrors the value into the fallback store")
    void getMirrorsValue() {
        redisOps.store.put("k1", "v1");

        ValueWrapper wrapper = cache.get("k1");

        assertThat(wrapper).isNotNull();
        assertThat(wrapper.get()).isEqualTo("v1");
        assertThat(fallbackStore.estimatedSize()).isEqualTo(1L);
    }

    @Test
    @DisplayName("get cache miss does not populate the fallback store")
    void getMissDoesNotMirror() {
        assertThat(cache.get("absent")).isNull();
        assertThat(fallbackStore.estimatedSize()).isZero();
    }

    @Test
    @DisplayName("put writes to Redis and mirrors into the fallback store")
    void putMirrorsValue() {
        cache.put("k1", "v1");

        assertThat(redisOps.store).containsEntry("k1", "v1");
        assertThat(fallbackStore.get(CACHE + "::k1").get()).isEqualTo("v1");
    }

    @Test
    @DisplayName("present-but-null value round-trips through the fallback store")
    void nullValueMirrored() {
        redisOps.store.put("k1", null);
        redisOps.presentKeys.add("k1");

        ValueWrapper wrapper = cache.get("k1");

        assertThat(wrapper).isNotNull();
        assertThat(wrapper.get()).isNull();
        assertThat(fallbackStore.estimatedSize()).isEqualTo(1L);
    }

    @Test
    @DisplayName("when Redis throws, the fallback mirror is served")
    void servesFallbackWhenRedisThrows() {
        cache.put("k1", "v1");  // warms the mirror
        redisOps.failing = true;

        ValueWrapper wrapper = cache.get("k1");

        assertThat(wrapper).isNotNull();
        assertThat(wrapper.get()).isEqualTo("v1");
    }

    @Test
    @DisplayName("when the circuit is OPEN, get serves the fallback without touching Redis")
    void servesFallbackWhenCircuitOpen() {
        cache.put("k1", "v1");
        circuitBreaker.transitionToOpenState();
        redisOps.failing = true; // would throw if Redis were actually called

        ValueWrapper wrapper = cache.get("k1");

        assertThat(wrapper).isNotNull();
        assertThat(wrapper.get()).isEqualTo("v1");
    }

    private static final class StubRedisCacheOps implements Cache, RedisCacheOps {
        private final Map<Object, Object> store = new HashMap<>();
        private final Set<Object> presentKeys = new HashSet<>();
        private boolean failing;

        @Override
        public ValueWrapper get(Object key) {
            failIfNeeded();
            if (store.containsKey(key) || presentKeys.contains(key)) {
                return new SimpleValueWrapper(store.get(key));
            }
            return null;
        }

        @Override
        public void put(Object key, Object value) {
            failIfNeeded();
            store.put(key, value);
        }

        @Override
        public void evict(Object key) {
            failIfNeeded();
            store.remove(key);
        }

        @Override
        public void clear() {
            failIfNeeded();
            store.clear();
        }

        @Override
        public String compositeKey(Object key) {
            return CACHE + "::" + key;
        }

        private void failIfNeeded() {
            if (failing) {
                throw new IllegalStateException("Redis unavailable");
            }
        }

        @Override
        public String getName() {
            return CACHE;
        }

        @Override
        public Object getNativeCache() {
            return this;
        }

        @Override
        public <T> T get(Object key, Class<T> type) {
            return null;
        }

        @Override
        public <T> T get(Object key, Callable<T> valueLoader) {
            return null;
        }
    }
}

