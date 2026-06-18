package com.rubal.caching.resilient;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.cache.Cache.ValueWrapper;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit tests for {@link FallbackStore} covering composite-key storage, null handling, eviction, and
 * clearing.
 */
@DisplayName("FallbackStore")
class FallbackStoreTest {

    private FallbackStore store;

    @BeforeEach
    void setUp() {
        store = new FallbackStore(100, 0);
    }

    @Test
    @DisplayName("absent key returns null wrapper")
    void absentReturnsNull() {
        assertThat(store.get("c1::missing")).isNull();
    }

    @Test
    @DisplayName("stored value round-trips")
    void storedValueRoundTrips() {
        store.put("c1::k1", "v1");

        ValueWrapper wrapper = store.get("c1::k1");
        assertThat(wrapper).isNotNull();
        assertThat(wrapper.get()).isEqualTo("v1");
    }

    @Test
    @DisplayName("present-but-null value returns a non-null wrapper holding null")
    void nullValueRoundTrips() {
        store.put("c1::k1", null);

        ValueWrapper wrapper = store.get("c1::k1");
        assertThat(wrapper).isNotNull();
        assertThat(wrapper.get()).isNull();
    }

    @Test
    @DisplayName("same user key in different caches does not collide")
    void cacheNamespacing() {
        store.put("c1::k", "from-c1");
        store.put("c2::k", "from-c2");

        assertThat(store.get("c1::k").get()).isEqualTo("from-c1");
        assertThat(store.get("c2::k").get()).isEqualTo("from-c2");
    }

    @Test
    @DisplayName("evict removes only the targeted entry")
    void evictRemovesEntry() {
        store.put("c1::k1", "v1");
        store.put("c1::k2", "v2");

        store.evict("c1::k1");

        assertThat(store.get("c1::k1")).isNull();
        assertThat(store.get("c1::k2")).isNotNull();
    }

    @Test
    @DisplayName("evict of a null key is a no-op")
    void evictNullKeyIsNoOp() {
        store.put("c1::k1", "v1");

        store.evict(null);

        assertThat(store.get("c1::k1")).isNotNull();
    }

    @Test
    @DisplayName("clear wipes all entries across caches")
    void clearWipesEverything() {
        store.put("c1::k1", "v1");
        store.put("c2::k2", "v2");

        store.clear();

        assertThat(store.estimatedSize()).isZero();
        assertThat(store.get("c1::k1")).isNull();
        assertThat(store.get("c2::k2")).isNull();
    }
}

