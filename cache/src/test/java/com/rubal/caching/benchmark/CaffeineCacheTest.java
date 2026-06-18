package com.rubal.caching.benchmark;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.cache.caffeine.CaffeineCache;

import static org.assertj.core.api.Assertions.assertThat;

@Disabled (" Execute only local for time comparisons")
public class CaffeineCacheTest {

    private CaffeineCache inMemoryCache;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setup() {
        objectMapper = new ObjectMapper();
        inMemoryCache = new CaffeineCache("test", Caffeine.newBuilder().build());
    }

    @Test
    void testGetName() {
        var name = inMemoryCache.getName();
        assertThat(name).isEqualTo("test");
    }

    @Test
    @Disabled("Used only to test performance")
    void testIncreaseSlowRetrieval() {
        for (int i = 0; i < Integer.MAX_VALUE; i++) {
            var key = String.valueOf(i) + i;

            // Saving
            var initial = System.currentTimeMillis();
            inMemoryCache.put( key, new InMemoryCacheTest.TestData(i, String.valueOf(i)));
            var totalTime = System.currentTimeMillis() - initial;
            if(totalTime > 200)
                System.out.println(i + " Total time to save: " + totalTime + " ms");
            if(totalTime > 500) {
                break;
            }

            // Retrieving
            initial = System.currentTimeMillis();
            inMemoryCache.get(key);
            totalTime = System.currentTimeMillis() - initial;
            if(totalTime > 200)
                System.out.println(i + " Total time to retrieve: " + totalTime + " ms");
            if(totalTime > 500) {
                break;
            }
        }
    }

    record TestData(int num, String val) {
    }
}
