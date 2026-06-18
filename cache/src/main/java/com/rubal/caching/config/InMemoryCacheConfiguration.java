package com.rubal.caching.config;

import com.rubal.caching.indexing.IndexableRepository;
import com.rubal.caching.mem.InMemoryIndexableRepository;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cache.annotation.CachingConfigurer;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.util.List;

/**
 * cache manager for configuration support
 */
@Configuration
@ConditionalOnProperty(value = "spring.cache.type", matchIfMissing = true, havingValue = "mem")
public class InMemoryCacheConfiguration implements CachingConfigurer {

    private static final Logger logger = LoggerFactory.getLogger(InMemoryCacheConfiguration.class);

    public InMemoryCacheConfiguration(CachingConfiguration cachingConfiguration, List<IndexableRepository> indexableRepositories){
        indexableRepositories.forEach(cachingConfiguration::addSearchableCache);
    }
    /**
     * constructor
     */
    public InMemoryCacheConfiguration() {
    }

    /**
     * memory cache manager instantiate
     *
     * @return memory cache manager
     */
    @Bean
    @Primary
    public org.springframework.cache.CacheManager memCacheManager(CachingConfiguration cachingConfiguration) {
        System.out.println("\n================>LOG ENABLED Caffeine CacheManager\n");
        CaffeineCacheManager caffeineCacheManager = new CaffeineCacheManager();
        cachingConfiguration.getCacheInfos().forEach(cacheInfo -> {
            if (cacheInfo.getTtl() > 0) {
                caffeineCacheManager.registerCustomCache(
                        cacheInfo.getName(),
                        Caffeine.newBuilder().expireAfterWrite(CachingConfiguration.getDuration(cacheInfo)).build()
                );
            }
        });

        return caffeineCacheManager;
    }

    @Bean
    public IndexableRepository indexableRepository() {
        return new InMemoryIndexableRepository();
    }

}
