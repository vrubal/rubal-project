package com.rubal.caching.redis;

import com.rubal.caching.config.CachingConfiguration;
import com.rubal.caching.model.CacheInfo;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import redis.clients.jedis.JedisPooled;
import redis.clients.jedis.RedisClient;

import java.util.Collection;
import java.util.Collections;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Spring {@link CacheManager} backed by {@link JedisPooled} with Jedis client-side caching (CSC).
 * <p>
 * Pre-creates cache instances from {@link CachingConfiguration} and supports on-demand creation
 * for cache names not defined at startup.
 */
public class JedisCSCCacheManager implements CacheManager {

    private final ConcurrentMap<String, Cache> caches = new ConcurrentHashMap<>();
    private final RedisClient jedisPooled;
    private final ESFSerializer serializer;

    public JedisCSCCacheManager(RedisClient jedisPooled, ESFSerializer serializer,
                                CachingConfiguration cachingConfiguration) {
        this.jedisPooled = jedisPooled;
        this.serializer = serializer;

        for (CacheInfo ci : cachingConfiguration.getCacheInfos()) {
            long ttlSeconds = CachingConfiguration.getDuration(ci).getSeconds();
            caches.put(ci.getName(),
                    new JedisCSCCache(ci.getName(), jedisPooled, serializer, ttlSeconds));
        }
    }

    @Override
    public Cache getCache(String name) {
        return caches.computeIfAbsent(name,
                n -> new JedisCSCCache(n, jedisPooled, serializer, 0));
    }

    @Override
    public Collection<String> getCacheNames() {
        return Collections.unmodifiableSet(caches.keySet());
    }
}
