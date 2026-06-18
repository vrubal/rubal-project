package com.rubal.caching.mem;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rubal.caching.model.CacheInfo;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * in memory cache manager
 */
public class InMemoryCacheManager implements CacheManager {

    private final HashMap<String, InMemoryCache> caches = new HashMap<>();

    /**
     * instantiate with collection of cache names
     * @param names cache names
     */
    InMemoryCacheManager(Collection<String> names, List<CacheInfo> cacheInfos, ObjectMapper objectMapper) {
        names.forEach(name ->  {
            var ttl = getTtl(cacheInfos, name);
            caches.put(name, new InMemoryCache(name, ttl, objectMapper));
        });
    }

    /**
     *  get the ttl , currently set to 0 (no ttl)
     * @param cacheInfos cache info list
     * @param name the name of this cache
     * @return the ttl of this cache
     */
    private long getTtl(List<CacheInfo> cacheInfos, String name) {
        var ttl = 0L; //24 * 60 * 60 * 1000;
        for (CacheInfo ci : cacheInfos) {
            if (ci.getName().equals(name)) {
                ttl = toTtl(ci);
                break;
            }
        }
        return ttl;
    }

    /**
     *  convert the cache units to a number
     * @param ci the cache info object
     * @return the ttl
     */
    private long toTtl(CacheInfo ci) {
        var ttl = ci.getTtl();
        if (ttl == -1) return -1;
        if (ttl <= 0) return -1;
        if (ci.getUnit().equals(TimeUnit.DAYS)) return (ttl * 24 * 60 * 60 * 1000);
        if (ci.getUnit().equals(TimeUnit.HOURS)) return (ttl * 60 * 60 * 1000);
        if (ci.getUnit().equals(TimeUnit.MINUTES)) return (ttl * 60 * 1000);
        if (ci.getUnit().equals(TimeUnit.SECONDS)) return (ttl * 1000);
        if (ci.getUnit().equals(TimeUnit.MILLISECONDS)) return ttl;
        if (ci.getUnit().equals(TimeUnit.MICROSECONDS)) return (ttl/1000);
        if (ci.getUnit().equals(TimeUnit.NANOSECONDS)) return (ttl/1000000);
        return ttl;
    }

    /**
     * get cache for name
     * @param s cache name
     * @return cache
     */
    @Override
    public Cache getCache(String s) { return caches.get(s); }

    /**
     * get cache names
     * @return collection of cache names
     */
    @Override
    public Collection<String> getCacheNames() {
        return caches.keySet();
    }
}
