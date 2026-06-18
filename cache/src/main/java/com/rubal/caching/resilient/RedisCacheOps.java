package com.rubal.caching.resilient;

import org.springframework.cache.Cache.ValueWrapper;

/**
 * Minimal abstraction over the raw Redis cache operations performed by {@link RedisCacheGateway}.
 * <p>
 * Deliberately mirrors the subset of Spring's {@link org.springframework.cache.Cache} contract that
 * the resilient layer needs, so the Redis-backed {@code JedisCSCCache} satisfies it without an
 * adapter, and so the gateway can be unit-tested with a simple stub. Each implementation knows its
 * own cache name and TTL, so keys passed here are the caller's <em>user</em> keys.
 */
public interface RedisCacheOps {

    /**
     * @param key the user cache key
     * @return the value wrapper, or {@code null} if absent
     */
    ValueWrapper get(Object key);

    /**
     * @param key   the user cache key
     * @param value the value to store
     */
    void put(Object key, Object value);

    /**
     * @param key the user cache key to remove
     */
    void evict(Object key);

    /**
     * Removes every key belonging to this cache.
     */
    void clear();

    /**
     * Computes the composite Redis key string ({@code cacheName::userKey}) for the given user key,
     * using the same encoding the Redis-backed cache uses. This lets the resilient layer key its
     * fallback store identically to Redis, so CSC invalidations (which carry the composite key) line
     * up with mirrored entries.
     *
     * @param key the user cache key
     * @return the composite Redis key string
     */
    String compositeKey(Object key);
}

