package com.rubal.caching.redis;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fiserv.banksolutions.esf.client.caching.IndexableRoutingRuleRepository;
import com.fiserv.banksolutions.esf.client.caching.RoutingRuleCacheRepository;
import com.rubal.caching.config.CachingConfiguration;
import com.rubal.caching.config.InMemoryCacheConfiguration;
import com.rubal.caching.config.RedisConfiguration;
import com.rubal.caching.model.CacheInfo;
import com.rubal.caching.resilient.FallbackStore;
import com.rubal.caching.resilient.ResilientCacheManager;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.cache.CacheManager;
import org.springframework.core.env.Environment;
import org.springframework.test.util.ReflectionTestUtils;
import redis.clients.jedis.JedisPooled;
import redis.clients.jedis.RedisClient;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class RedisCacheTest {

    private ObjectMapper objectMapper;
    private ESFSerializer serializer;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper().findAndRegisterModules();
        serializer = new ESFSerializer(objectMapper);
    }

    // =========================================================================
    // CacheConverter tests
    // =========================================================================
    @Nested
    @DisplayName("CacheConverter")
    class CacheConverterTests {

        @Test
        @DisplayName("should convert object to JSON string")
        void testCacheConverter() {
            CacheConverter converter = new CacheConverter(objectMapper);
            String json = converter.convert(new TestObject("test"));
            assertEquals("{\"name\":\"test\"}", json);
        }

        @Test
        @DisplayName("should convert object with null field")
        void testCacheConverterNullField() {
            CacheConverter converter = new CacheConverter(objectMapper);
            String json = converter.convert(new TestObject(null));
            assertEquals("{\"name\":null}", json);
        }
    }

    // =========================================================================
    // ESFSerializer tests
    // =========================================================================
    @Nested
    @DisplayName("ESFSerializer")
    class ESFSerializerTests {

        @Test
        @DisplayName("should serialize object to bytes")
        void testSerialize() {
            byte[] bytes = serializer.serialize(new TestObject("test"));
            assertNotNull(bytes);
            assertTrue(bytes.length > 0);
        }

        @Test
        @DisplayName("should deserialize bytes back to object")
        void testDeserialize() {
            byte[] bytes = serializer.serialize(new TestObject("test"));
            Object obj = serializer.deserialize(bytes);
            assertNotNull(obj);
        }

        @Test
        @DisplayName("should return null when serializing null")
        void testSerializeNull() {
            byte[] bytes = serializer.serialize(null);
            assertNull(bytes);
        }

        @Test
        @DisplayName("should return null when deserializing null")
        void testDeserializeNull() {
            Object obj = serializer.deserialize(null);
            assertNull(obj);
        }

        @Test
        @DisplayName("should return null when deserializing empty byte array")
        void testDeserializeEmpty() {
            Object obj = serializer.deserialize(new byte[0]);
            assertNull(obj);
        }

        @Test
        @DisplayName("should serialize final type via ESFWrapper")
        void testSerializeFinalType() {
            // String is final, so ESFSerializer wraps it in ESFWrapper
            byte[] bytes = serializer.serialize("hello");
            assertNotNull(bytes);
            assertTrue(bytes.length > 0);
            // Deserialization without polymorphic typing returns a map;
            // full unwrapping requires the RedisConfiguration's ObjectMapper
            Object result = serializer.deserialize(bytes);
            assertNotNull(result);
        }

        @Test
        @DisplayName("should serialize as string")
        void testSerializeAsString() throws Exception {
            String json = serializer.serializeAsString(new TestObject("test"));
            assertNotNull(json);
            assertThat(json).contains("\"name\":\"test\"");
        }

        @Test
        @DisplayName("should deserialize from string with type")
        void testDeserializeWithType() throws Exception {
            String json = "{\"name\":\"test\"}";
            TestObject obj = serializer.deserialize(json, TestObject.class);
            assertNotNull(obj);
            assertEquals("test", obj.getName());
        }
    }

    // =========================================================================
    // InMemoryCacheConfiguration tests
    // =========================================================================
    @Nested
    @DisplayName("InMemoryCacheConfiguration")
    class InMemoryCacheTests {

        @Test
        @DisplayName("should create Caffeine cache manager with configured caches")
        void testMemCacheManager() {
            CachingConfiguration cachingConfiguration = new CachingConfiguration(
                    List.of(new RoutingRuleCacheRepository()));
            InMemoryCacheConfiguration memConfig = new InMemoryCacheConfiguration();

            CacheManager cacheManager = memConfig.memCacheManager(cachingConfiguration);

            // RoutingRule has TTL 0, so Caffeine won't pre-register it as a custom cache.
            // But CaffeineCacheManager creates caches on demand via getCache().
            assertThat(cacheManager.getCache("RoutingRule")).isNotNull();
        }
    }

    // =========================================================================
    // RedisConfiguration - redisCacheManager (mocked JedisPooled)
    // =========================================================================
    @Nested
    @DisplayName("RedisConfiguration - redisCacheManager")
    @ExtendWith(MockitoExtension.class)
    class RedisCacheManagerTests {

        @Mock
        private Environment environment;

        @Mock
        private RedisClient redisClient;

        @Mock
        private CachingConfiguration cachingConfiguration;

        private final FallbackStore fallbackStore = new FallbackStore(100, 0);
        private final CircuitBreakerRegistry circuitBreakerRegistry = CircuitBreakerRegistry.ofDefaults();

        @Test
        @DisplayName("should create ResilientCacheManager with correct cache entries")
        void testRedisCacheManager() {
            String cache1 = "cache" + UUID.randomUUID();
            String cache2 = "cache" + UUID.randomUUID();

            List<CacheInfo> cacheInfos = Arrays.asList(
                    new CacheInfo(cache1, 1L, TimeUnit.HOURS),
                    new CacheInfo(cache2, 30L, TimeUnit.MINUTES),
                    new CacheInfo("cache" + UUID.randomUUID(), 1L, TimeUnit.SECONDS),
                    new CacheInfo("cache" + UUID.randomUUID(), 1L, TimeUnit.MILLISECONDS),
                    new CacheInfo("cache" + UUID.randomUUID(), 1L, TimeUnit.MICROSECONDS),
                    new CacheInfo("cache" + UUID.randomUUID(), 1L, TimeUnit.NANOSECONDS),
                    new RoutingRuleCacheRepository().getCacheInfo()
            );

            when(cachingConfiguration.getCacheInfos()).thenReturn(cacheInfos);
            when(cachingConfiguration.getSearchableCaches()).thenReturn(List.of());

            RedisConfiguration redisConfig = new RedisConfiguration(environment);
            setAllRedisFields(redisConfig);

            CacheManager cacheManager = redisConfig.redisCacheManager(
                    cachingConfiguration, List.of(), redisClient, serializer, fallbackStore, circuitBreakerRegistry);

            assertThat(cacheManager).isInstanceOf(ResilientCacheManager.class);
            assertThat(cacheManager.getCacheNames()).hasSize(7);
            assertThat(cacheManager.getCache(cache1)).isNotNull();
            assertThat(cacheManager.getCache(cache2)).isNotNull();
        }

        @Test
        @DisplayName("should handle empty cache configuration")
        void testRedisCacheManagerEmpty() {
            when(cachingConfiguration.getCacheInfos()).thenReturn(List.of());
            when(cachingConfiguration.getSearchableCaches()).thenReturn(List.of());

            RedisConfiguration redisConfig = new RedisConfiguration(environment);
            setAllRedisFields(redisConfig);

            CacheManager cacheManager = redisConfig.redisCacheManager(
                    cachingConfiguration, List.of(), redisClient, serializer, fallbackStore, circuitBreakerRegistry);

            assertThat(cacheManager).isInstanceOf(ResilientCacheManager.class);
            assertThat(cacheManager.getCacheNames()).isEmpty();
        }

        @Test
        @DisplayName("should create on-demand cache for unknown name")
        void testOnDemandCacheCreation() {
            when(cachingConfiguration.getCacheInfos()).thenReturn(List.of());
            when(cachingConfiguration.getSearchableCaches()).thenReturn(List.of());

            RedisConfiguration redisConfig = new RedisConfiguration(environment);
            setAllRedisFields(redisConfig);

            CacheManager cacheManager = redisConfig.redisCacheManager(
                    cachingConfiguration, List.of(), redisClient, serializer, fallbackStore, circuitBreakerRegistry);

            assertThat(cacheManager.getCache("dynamicNewCache")).isNotNull();
        }

        private void setAllRedisFields(RedisConfiguration config) {
            ReflectionTestUtils.setField(config, "host", "localhost");
            ReflectionTestUtils.setField(config, "port", 6379);
            ReflectionTestUtils.setField(config, "pwd", "password");
            ReflectionTestUtils.setField(config, "timeout", 5000);
            ReflectionTestUtils.setField(config, "connectTimeout", 60000);
            ReflectionTestUtils.setField(config, "database", 0);
            ReflectionTestUtils.setField(config, "clientName", "/test-app");
            ReflectionTestUtils.setField(config, "poolMaxActive", 4);
            ReflectionTestUtils.setField(config, "poolMaxIdle", 4);
            ReflectionTestUtils.setField(config, "poolMinIdle", 2);
            ReflectionTestUtils.setField(config, "poolTestWhileIdle", true);
            ReflectionTestUtils.setField(config, "timeBetweenEvictionRuns", 15000L);
            ReflectionTestUtils.setField(config, "blockWhenExhausted", true);
            ReflectionTestUtils.setField(config, "maxWait", 1000L);
            ReflectionTestUtils.setField(config, "cscMaxSize", 10000);
            ReflectionTestUtils.setField(config, "cscKeyPrefixes", List.of());
        }
    }

    @Setter
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    static class TestObject {
        private String name;
    }

}


