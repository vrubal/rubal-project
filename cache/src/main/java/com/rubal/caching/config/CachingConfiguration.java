package com.rubal.caching.config;

import com.rubal.caching.indexing.IndexableRepository;
import com.rubal.caching.model.CacheInfo;
import com.rubal.caching.model.IndexableCacheInfo;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Configuration settings for the cache including
 * <ul>
 * <li>type</li>
 * <li>host</li>
 * <li>port</li>
 * </ul>
 */
@Getter
@Configuration
@EnableCaching
public class CachingConfiguration {

    private static final Logger logger = LoggerFactory.getLogger(CachingConfiguration.class);
    /**
     * list of cache names
     */
    private final ArrayList<String> caches;

    // TODO: Not sure why we need duplicate list of cache names
    /**
     * synchronized list of global cache
     */
    private final List<String> globalCaches = Collections.synchronizedList(new ArrayList<>());

    /**
     * global list of cache info objects
     * -- GETTER --
     * the cache info list
     */
    @Getter
    private List<CacheInfo> cacheInfos = Collections.synchronizedList(new ArrayList<>());

    /**
     * Defines a list of caches for which search indexes will be created
     */
    @Getter
    private List<IndexableCacheInfo> searchableCaches = Collections.synchronizedList(new ArrayList<>());

    /**
     * creates the list of all caches
     */
    public CachingConfiguration(List<CustomCacheRepository> customCacheRepositories) {
        caches = new ArrayList<>();
        caches.addAll(globalCaches);
        customCacheRepositories.forEach(cacheRepository -> {
            CacheInfo cacheInfo = cacheRepository.getCacheInfo();
            caches.add(cacheInfo.getName());
            globalCaches.add(cacheInfo.getName());
            cacheInfos.add(cacheInfo);
        });
    }

    public void addSearchableCache(IndexableRepository indexableRepository) {
        if (indexableRepository.isIndexable()) searchableCaches.add(indexableRepository.getIndexableCacheInfo());
    }

    /**
     * gets the duration ttl of the cache
     *
     * @param ci the cache info
     * @return the duration in units
     */
    public static Duration getDuration(CacheInfo ci) {
        return switch (ci.getUnit()) {
            case DAYS -> Duration.ofDays(ci.getTtl());
            case HOURS -> Duration.ofHours(ci.getTtl());
            case MINUTES -> Duration.ofMinutes(ci.getTtl());
            case SECONDS -> Duration.ofSeconds(ci.getTtl());
            case MICROSECONDS -> Duration.ofNanos(1000 * ci.getTtl());
            case NANOSECONDS -> Duration.ofNanos(ci.getTtl());
            default -> Duration.ofMillis(ci.getTtl());
        };
    }

}