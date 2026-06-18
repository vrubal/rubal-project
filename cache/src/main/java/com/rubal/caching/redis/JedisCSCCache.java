package com.rubal.caching.redis;

import com.rubal.caching.resilient.RedisCacheOps;
import org.springframework.cache.Cache;
import org.springframework.cache.support.SimpleValueWrapper;
import redis.clients.jedis.JedisPooled;
import redis.clients.jedis.RedisClient;
import redis.clients.jedis.params.ScanParams;
import redis.clients.jedis.params.SetParams;

import java.util.Arrays;
import java.util.concurrent.Callable;

/**
 * Spring {@link Cache} implementation backed by {@link JedisPooled} with Jedis client-side caching (CSC).
 * <p>
 * All read operations go through JedisPooled which transparently serves from the local CSC
 * when available, with server-assisted invalidation when keys are modified by other clients.
 * <p>
 * Also implements {@link RedisCacheOps}: since its {@code get/put/evict/clear} methods already match
 * that contract, the resilient circuit-breaker gateway can drive raw Redis access through the same
 * instance without an adapter.
 */
public class JedisCSCCache implements Cache, RedisCacheOps {

    private final String name;
    private final RedisClient jedisPooled;
    private final ESFSerializer serializer;
    private final long ttlSeconds;
    public static final String KEY_SEPARATOR = "::";

    public JedisCSCCache(String name, RedisClient jedisPooled, ESFSerializer serializer, long ttlSeconds) {
        this.name = name;
        this.jedisPooled = jedisPooled;
        this.serializer = serializer;
        this.ttlSeconds = ttlSeconds;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Object getNativeCache() {
        return jedisPooled;
    }

    @Override
    public ValueWrapper get(Object key) {
        byte[] raw = jedisPooled.get(buildKey(key));
        if (raw == null) {
            return null;
        }
        Object value = serializer.deserialize(raw);
        return new SimpleValueWrapper(value);
    }

    @SuppressWarnings("unchecked")
    @Override
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

    @SuppressWarnings("unchecked")
    @Override
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
        byte[] redisKey = buildKey(key);
        byte[] serialized = serializer.serialize(value);
        // Avoid the null values against a key in the cache
        if (serialized == null) {
            return;
        }
        if (ttlSeconds > 0) {
            jedisPooled.set(redisKey, serialized, SetParams.setParams().ex(ttlSeconds));
        } else {
            jedisPooled.set(redisKey, serialized);
        }
    }

    @Override
    public void evict(Object key) {
        jedisPooled.del(buildKey(key));
    }

    @Override
    public void clear() {
        byte[] pattern = (name + KEY_SEPARATOR + "*").getBytes();
        ScanParams params = new ScanParams().match(pattern).count(100);
        byte[] cursor = ScanParams.SCAN_POINTER_START_BINARY;
        do {
            var result = jedisPooled.scan(cursor, params);
            var keys = result.getResult();
            if (!keys.isEmpty()) {
                jedisPooled.del(keys.toArray(new byte[0][]));
            }
            cursor = result.getCursorAsBytes();
        } while (!Arrays.equals(cursor, ScanParams.SCAN_POINTER_START_BINARY));
    }

    private byte[] buildKey(Object key) {
        return compositeKey(key).getBytes();
    }

    /**
     * Builds the composite Redis key string ({@code name::userKey}) for the given user key. String
     * keys are used verbatim; other keys are serialized to JSON, falling back to {@code toString()}.
     *
     * @param key the user cache key
     * @return the composite Redis key string
     */
    @Override
    public String compositeKey(Object key) {
        String keyStr;
        if (key instanceof String s) {
            keyStr = s;
        } else {
            try {
                keyStr = serializer.serializeAsString(key);
            } catch (Exception e) {
                keyStr = key.toString();
            }
        }
        return name + KEY_SEPARATOR + keyStr;
    }
}
