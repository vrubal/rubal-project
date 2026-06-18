package com.rubal.caching.resilient;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.cache.Cache.ValueWrapper;
import org.springframework.cache.support.SimpleValueWrapper;

import java.time.Duration;

/**
 * Single shared in-memory mirror used by the resilient caches while Redis is unavailable.
 * <p>
 * Backed by one bounded Caffeine cache shared across every cache, keyed by the composite Redis key
 * string ({@code cacheName::userKey}). This gives a single global memory ceiling and an O(1) atomic
 * {@link #clear()} for circuit recovery. A {@link #NULL_SENTINEL} represents stored {@code null}
 * values, since Caffeine forbids nulls while the Spring cache contract must distinguish "absent"
 * from "present but null".
 */
public class FallbackStore {

    /** Sentinel representing a {@code null} value held in the cache. */
    private static final Object NULL_SENTINEL = new Object();

    private final Cache<String, Object> cache;

    /**
     * @param maxSize    maximum number of mirrored entries before eviction
     * @param ttlSeconds time-to-live for entries in seconds; {@code 0} disables TTL
     */
    public FallbackStore(long maxSize, long ttlSeconds) {
        Caffeine<Object, Object> builder = Caffeine.newBuilder().maximumSize(maxSize);
        if (ttlSeconds > 0) {
            builder.expireAfterWrite(Duration.ofSeconds(ttlSeconds));
        }
        this.cache = builder.build();
    }

    /**
     * @param compositeKey the composite {@code cacheName::userKey} key
     * @return a wrapper holding the mirrored value (which may be {@code null}), or {@code null} if absent
     */
    public ValueWrapper get(String compositeKey) {
        Object cached = cache.getIfPresent(compositeKey);
        return cached == null ? null : new SimpleValueWrapper(unwrap(cached));
    }

    /** Mirrors a value (which may be {@code null}) under the composite key. */
    public void put(String compositeKey, Object value) {
        cache.put(compositeKey, wrap(value));
    }

    /** Removes a single mirrored entry. */
    public void evict(String compositeKey) {
        if (compositeKey != null) {
            cache.invalidate(compositeKey);
        }
    }

    /** Wipes every mirrored entry across all caches. */
    public void clear() {
        cache.invalidateAll();
    }

    /** @return an approximate count of mirrored entries (for diagnostics and tests). */
    public long estimatedSize() {
        return cache.estimatedSize();
    }

    private static Object wrap(Object value) {
        return value == null ? NULL_SENTINEL : value;
    }

    private static Object unwrap(Object value) {
        return value == NULL_SENTINEL ? null : value;
    }
}

