package com.rubal.caching.mem;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rubal.caching.model.CacheInfo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.cache.Cache;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNull;

public class InMemoryCacheManagerTest {

    private InMemoryCacheManager cacheManager;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        List<CacheInfo> cacheInfos = Arrays.asList(
                new CacheInfo("cache1", 1L, TimeUnit.HOURS),
                new CacheInfo("cache2", 30L, TimeUnit.MINUTES)
        );
        cacheManager = new InMemoryCacheManager(Arrays.asList("cache1", "cache2"), cacheInfos, objectMapper);
    }

    @Test
    void testGetCache() {
        Cache cache1 = cacheManager.getCache("cache1");
        assertThat(cache1).isNotNull();
        assertThat(cache1.getName()).isEqualTo("cache1");

        Cache cache2 = cacheManager.getCache("cache2");
        assertThat(cache2).isNotNull();
        assertThat(cache2.getName()).isEqualTo("cache2");

        Cache cache3 = cacheManager.getCache("cache3");
        assertNull(cache3);
    }

    @Test
    void testGetCacheNames() {
        Collection<String> cacheNames = cacheManager.getCacheNames();
        assertThat(cacheNames).containsExactlyInAnyOrder("cache1", "cache2");
    }

    @Test
    void testGetTtl() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        List<CacheInfo> cacheInfos = Collections.singletonList(new CacheInfo("cache1", 1L, TimeUnit.HOURS));
        Method method = InMemoryCacheManager.class.getDeclaredMethod("getTtl", List.class, String.class);
        method.setAccessible(true);

        long ttl = (long) method.invoke(cacheManager, cacheInfos, "cache1");
        assertThat(ttl).isEqualTo(3600000); // 1 hour in milliseconds
    }

    @ParameterizedTest
    @CsvSource({
            "1, DAYS, 86400000",
            "1, HOURS, 3600000",
            "1, MINUTES, 60000",
            "1, SECONDS, 1000",
            "1, MILLISECONDS, 1",
            "1, MICROSECONDS, 0",
            "1, NANOSECONDS, 0",
            "-1, HOURS, -1",
            "0, HOURS, -1"
    })
    void testToTtl(long ttlValue, TimeUnit unit, long expectedTtl) throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        CacheInfo cacheInfo = new CacheInfo("cache1", ttlValue, unit);
        Method method = InMemoryCacheManager.class.getDeclaredMethod("toTtl", CacheInfo.class);
        method.setAccessible(true);

        long ttl = (long) method.invoke(cacheManager, cacheInfo);
        assertThat(ttl).isEqualTo(expectedTtl);
    }
}