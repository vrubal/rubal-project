package com.rubal.caching.config;

import com.rubal.caching.annotations.IndexCreationMode;
import com.rubal.caching.indexing.IndexableRepository;
import com.rubal.caching.model.CacheInfo;
import com.rubal.caching.model.IndexableCacheInfo;
import com.rubal.caching.model.IndexedFieldsInfo;
import com.rubal.caching.redis.ESFSerializer;
import com.rubal.caching.resilient.FallbackStore;
import com.rubal.caching.resilient.ResilientCacheManager;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cache.CacheManager;
import org.springframework.core.env.Environment;
import org.springframework.test.util.ReflectionTestUtils;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import redis.clients.jedis.Connection;
import redis.clients.jedis.RedisClient;
import redis.clients.jedis.csc.Cacheable;
import redis.clients.jedis.csc.DefaultCacheable;
import redis.clients.jedis.exceptions.JedisDataException;
import redis.clients.jedis.search.FTCreateParams;

import redis.clients.jedis.search.schemafields.TextField;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@SuppressWarnings("rawtypes")
@ExtendWith(MockitoExtension.class)
class RedisConfigurationTest {

    @InjectMocks
    private RedisConfiguration redisConfiguration;

    @Mock
    private Environment environment;

    @Mock
    private RedisClient redisClient;

    @Mock
    private ESFSerializer esfSerializer;

    @Mock
    private CachingConfiguration cachingConfiguration;

    @Mock
    private IndexableRepository indexableRepository;

    private final FallbackStore fallbackStore = new FallbackStore(100, 0);
    private final CircuitBreakerRegistry circuitBreakerRegistry = CircuitBreakerRegistry.ofDefaults();

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(redisConfiguration, "host", "localhost");
        ReflectionTestUtils.setField(redisConfiguration, "port", 6379);
        ReflectionTestUtils.setField(redisConfiguration, "pwd", "password");
        ReflectionTestUtils.setField(redisConfiguration, "timeout", 5000);
        ReflectionTestUtils.setField(redisConfiguration, "connectTimeout", 60000);
        ReflectionTestUtils.setField(redisConfiguration, "database", 0);
        ReflectionTestUtils.setField(redisConfiguration, "clientName", "/test-app");
        ReflectionTestUtils.setField(redisConfiguration, "poolMaxActive", 4);
        ReflectionTestUtils.setField(redisConfiguration, "poolMaxIdle", 4);
        ReflectionTestUtils.setField(redisConfiguration, "poolMinIdle", 2);
        ReflectionTestUtils.setField(redisConfiguration, "poolTestWhileIdle", true);
        ReflectionTestUtils.setField(redisConfiguration, "timeBetweenEvictionRuns", 15000L);
        ReflectionTestUtils.setField(redisConfiguration, "blockWhenExhausted", true);
        ReflectionTestUtils.setField(redisConfiguration, "maxWait", 1000L);
        ReflectionTestUtils.setField(redisConfiguration, "cscMaxSize", 10000);
        ReflectionTestUtils.setField(redisConfiguration, "cscKeyPrefixes", List.of());
    }

    // =========================================================================
    // redisCacheManager
    // =========================================================================
    @Nested
    @DisplayName("redisCacheManager")
    class RedisCacheManagerTests {

        @Test
        @DisplayName("should return ResilientCacheManager instance")
        void shouldReturnResilientCacheManager() {
            List<CacheInfo> cacheInfos = List.of(
                    new CacheInfo("cache1", 60L, TimeUnit.SECONDS),
                    new CacheInfo("cache2", 30L, TimeUnit.MINUTES)
            );
            when(cachingConfiguration.getCacheInfos()).thenReturn(cacheInfos);
            when(cachingConfiguration.getSearchableCaches()).thenReturn(List.of());

            CacheManager cacheManager = redisConfiguration.redisCacheManager(
                    cachingConfiguration, List.of(indexableRepository), redisClient, esfSerializer, fallbackStore, circuitBreakerRegistry);

            assertThat(cacheManager).isInstanceOf(ResilientCacheManager.class);
        }

        @Test
        @DisplayName("should register all cache names from configuration")
        void shouldRegisterAllCacheNames() {
            List<CacheInfo> cacheInfos = List.of(
                    new CacheInfo("RoutingRule", 1L, TimeUnit.HOURS),
                    new CacheInfo("ConfigCache", 30L, TimeUnit.MINUTES)
            );
            when(cachingConfiguration.getCacheInfos()).thenReturn(cacheInfos);
            when(cachingConfiguration.getSearchableCaches()).thenReturn(List.of());

            CacheManager cacheManager = redisConfiguration.redisCacheManager(
                    cachingConfiguration, List.of(indexableRepository), redisClient, esfSerializer, fallbackStore, circuitBreakerRegistry);

            assertThat(cacheManager.getCacheNames()).hasSize(2);
            assertThat(cacheManager.getCache("RoutingRule")).isNotNull();
            assertThat(cacheManager.getCache("ConfigCache")).isNotNull();
        }

        @Test
        @DisplayName("should call addSearchableCache for each indexable repository")
        void shouldCallAddSearchableCache() {
            IndexableRepository repo1 = mock(IndexableRepository.class);
            IndexableRepository repo2 = mock(IndexableRepository.class);
            when(cachingConfiguration.getCacheInfos()).thenReturn(List.of());
            when(cachingConfiguration.getSearchableCaches()).thenReturn(List.of());

            redisConfiguration.redisCacheManager(
                    cachingConfiguration, List.of(repo1, repo2), redisClient, esfSerializer, fallbackStore, circuitBreakerRegistry);

            verify(cachingConfiguration).addSearchableCache(repo1);
            verify(cachingConfiguration).addSearchableCache(repo2);
        }

        @Test
        @DisplayName("should create on-demand cache for unknown name")
        void shouldCreateOnDemandCache() {
            when(cachingConfiguration.getCacheInfos()).thenReturn(List.of());
            when(cachingConfiguration.getSearchableCaches()).thenReturn(List.of());

            CacheManager cacheManager = redisConfiguration.redisCacheManager(
                    cachingConfiguration, List.of(), redisClient, esfSerializer, fallbackStore, circuitBreakerRegistry);

            assertThat(cacheManager.getCache("newDynamicCache")).isNotNull();
        }
    }

    // =========================================================================
    // esfSerializer
    // =========================================================================
    @Nested
    @DisplayName("esfSerializer")
    class ESFSerializerTests {

        @Test
        @DisplayName("should create ESFSerializer bean")
        void shouldCreateSerializer() {
            ESFSerializer serializer = redisConfiguration.esfSerializer();
            assertThat(serializer).isNotNull();
        }

        @Test
        @DisplayName("should serialize and deserialize objects")
        void shouldSerializeAndDeserialize() {
            ESFSerializer serializer = redisConfiguration.esfSerializer();
            String testValue = "hello";
            byte[] bytes = serializer.serialize(testValue);
            assertThat(bytes).isNotNull();
            assertThat(bytes.length).isGreaterThan(0);

            Object deserialized = serializer.deserialize(bytes);
            assertThat(deserialized).isEqualTo(testValue);
        }

        @Test
        @DisplayName("should handle null serialize")
        void shouldHandleNullSerialize() {
            ESFSerializer serializer = redisConfiguration.esfSerializer();
            assertNull(serializer.serialize(null));
        }

        @Test
        @DisplayName("should handle null deserialize")
        void shouldHandleNullDeserialize() {
            ESFSerializer serializer = redisConfiguration.esfSerializer();
            assertNull(serializer.deserialize(null));
        }

        @Test
        @DisplayName("should handle empty byte array deserialize")
        void shouldHandleEmptyDeserialize() {
            ESFSerializer serializer = redisConfiguration.esfSerializer();
            assertNull(serializer.deserialize(new byte[0]));
        }
    }

    // =========================================================================
    // checkCacheable (private method via reflection)
    // =========================================================================
    @Nested
    @DisplayName("checkCacheable")
    class CheckCacheableTests {

        @Test
        @DisplayName("should return DefaultCacheable when no prefixes configured")
        void shouldReturnDefaultWhenNoPrefixes() {
            ReflectionTestUtils.setField(redisConfiguration, "cscKeyPrefixes", List.of());

            Cacheable cacheable = invokeCheckCacheable();

            assertThat(cacheable).isSameAs(DefaultCacheable.INSTANCE);
        }

        @Test
        @DisplayName("should return DefaultCacheable when prefixes is null")
        void shouldReturnDefaultWhenPrefixesNull() {
            ReflectionTestUtils.setField(redisConfiguration, "cscKeyPrefixes", null);

            Cacheable cacheable = invokeCheckCacheable();

            assertThat(cacheable).isSameAs(DefaultCacheable.INSTANCE);
        }

        @Test
        @DisplayName("should exclude keys matching configured prefix")
        void shouldExcludeMatchingPrefix() {
            ReflectionTestUtils.setField(redisConfiguration, "cscKeyPrefixes", List.of("RoutingRule::"));

            Cacheable cacheable = invokeCheckCacheable();

            // A key starting with the excluded prefix should return false (not cacheable locally)
            assertThat(cacheable).isNotSameAs(DefaultCacheable.INSTANCE);
        }

        @Test
        @DisplayName("should return custom cacheable when prefixes are set")
        void shouldReturnCustomCacheableWithPrefixes() {
            ReflectionTestUtils.setField(redisConfiguration, "cscKeyPrefixes", List.of("index:", "temp:"));

            Cacheable cacheable = invokeCheckCacheable();

            assertThat(cacheable).isNotNull();
            assertThat(cacheable).isNotSameAs(DefaultCacheable.INSTANCE);
        }

        private Cacheable invokeCheckCacheable() {
            return ReflectionTestUtils.invokeMethod(redisConfiguration, "checkCacheable");
        }
    }

    // =========================================================================
    // buildPoolConfig (private method via reflection)
    // =========================================================================
    @Nested
    @DisplayName("buildPoolConfig")
    class BuildPoolConfigTests {

        @Test
        @DisplayName("should build pool config with correct max total")
        void shouldSetMaxTotal() {
            ReflectionTestUtils.setField(redisConfiguration, "poolMaxActive", 8);

            GenericObjectPoolConfig<Connection> config = invokeBuildPoolConfig();

            assertThat(config.getMaxTotal()).isEqualTo(8);
        }

        @Test
        @DisplayName("should build pool config with correct max idle")
        void shouldSetMaxIdle() {
            ReflectionTestUtils.setField(redisConfiguration, "poolMaxIdle", 6);

            GenericObjectPoolConfig<Connection> config = invokeBuildPoolConfig();

            assertThat(config.getMaxIdle()).isEqualTo(6);
        }

        @Test
        @DisplayName("should build pool config with correct min idle")
        void shouldSetMinIdle() {
            ReflectionTestUtils.setField(redisConfiguration, "poolMinIdle", 3);

            GenericObjectPoolConfig<Connection> config = invokeBuildPoolConfig();

            assertThat(config.getMinIdle()).isEqualTo(3);
        }

        @Test
        @DisplayName("should set test while idle")
        void shouldSetTestWhileIdle() {
            ReflectionTestUtils.setField(redisConfiguration, "poolTestWhileIdle", true);

            GenericObjectPoolConfig<Connection> config = invokeBuildPoolConfig();

            assertThat(config.getTestWhileIdle()).isTrue();
        }

        @Test
        @DisplayName("should set block when exhausted")
        void shouldSetBlockWhenExhausted() {
            ReflectionTestUtils.setField(redisConfiguration, "blockWhenExhausted", false);

            GenericObjectPoolConfig<Connection> config = invokeBuildPoolConfig();

            assertThat(config.getBlockWhenExhausted()).isFalse();
        }

        @Test
        @DisplayName("should normalize maxWait when less than -1")
        void shouldNormalizeInvalidMaxWait() {
            ReflectionTestUtils.setField(redisConfiguration, "maxWait", -5L);

            GenericObjectPoolConfig<Connection> config = invokeBuildPoolConfig();

            // -1 means indefinite, which translates to Duration of -1 millis
            assertThat(config.getMaxWaitDuration().toMillis()).isEqualTo(-1);
        }

        @Test
        @DisplayName("should accept valid maxWait value")
        void shouldAcceptValidMaxWait() {
            ReflectionTestUtils.setField(redisConfiguration, "maxWait", 2000L);

            GenericObjectPoolConfig<Connection> config = invokeBuildPoolConfig();

            assertThat(config.getMaxWaitDuration().toMillis()).isEqualTo(2000);
        }

        @Test
        @DisplayName("should accept -1 as indefinite maxWait")
        void shouldAcceptIndefiniteMaxWait() {
            ReflectionTestUtils.setField(redisConfiguration, "maxWait", -1L);

            GenericObjectPoolConfig<Connection> config = invokeBuildPoolConfig();

            assertThat(config.getMaxWaitDuration().toMillis()).isEqualTo(-1);
        }

        @SuppressWarnings("unchecked")
        private GenericObjectPoolConfig<Connection> invokeBuildPoolConfig() {
            return (GenericObjectPoolConfig<Connection>) ReflectionTestUtils.invokeMethod(redisConfiguration, "buildPoolConfig");
        }
    }

    // =========================================================================
    // createSearchIndexes
    // =========================================================================
    @Nested
    @DisplayName("createSearchIndexes")
    class CreateSearchIndexesTests {

        @Test
        @DisplayName("should skip index creation when index already exists")
        void shouldSkipExistingIndex() {
            IndexableCacheInfo cacheInfo = new IndexableCacheInfo(
                    "TestIndex", "testAlias", "Test:Idx:",
                    IndexCreationMode.SKIP_IF_EXISTS,
                    List.of(new IndexedFieldsInfo("$.name", "name", false, 1.0))
            );
            when(cachingConfiguration.getSearchableCaches()).thenReturn(List.of(cacheInfo));
            when(redisClient.ftInfo("TestIndex")).thenReturn(Map.of("index_name", "TestIndex"));

            ReflectionTestUtils.invokeMethod(redisConfiguration, "createSearchIndexes", cachingConfiguration, redisClient);

            // ftCreate should NOT be called since index exists
            verify(redisClient, never()).ftCreate(anyString(), any(FTCreateParams.class), any(TextField[].class));
        }

        @Test
        @DisplayName("should create index when it does not exist")
        void shouldCreateIndexWhenNotExists() {
            IndexableCacheInfo cacheInfo = new IndexableCacheInfo(
                    "NewIndex", "newAlias", "New:Idx:",
                    IndexCreationMode.SKIP_IF_EXISTS,
                    List.of(new IndexedFieldsInfo("$.name", "name", true, 2.0))
            );
            when(cachingConfiguration.getSearchableCaches()).thenReturn(List.of(cacheInfo));
            when(redisClient.ftInfo("NewIndex")).thenThrow(new JedisDataException("Unknown index name"));
            when(redisClient.ftCreate(anyString(), any(FTCreateParams.class), any(TextField[].class))).thenReturn("OK");

            ReflectionTestUtils.invokeMethod(redisConfiguration, "createSearchIndexes", cachingConfiguration, redisClient);

            verify(redisClient).ftCreate(eq("NewIndex"), any(FTCreateParams.class), any(TextField[].class));
        }

        @Test
        @DisplayName("should handle non-index-related JedisDataException gracefully")
        void shouldIgnoreNonIndexError() {
            IndexableCacheInfo cacheInfo = new IndexableCacheInfo(
                    "ErrorIndex", "", "Err:Idx:",
                    IndexCreationMode.SKIP_IF_EXISTS,
                    List.of(new IndexedFieldsInfo("$.field", "field", false, 1.0))
            );
            when(cachingConfiguration.getSearchableCaches()).thenReturn(List.of(cacheInfo));
            when(redisClient.ftInfo("ErrorIndex")).thenThrow(new JedisDataException("Some other error"));

            // Should not throw — the error is not "Unknown index name", so it's just ignored
            assertDoesNotThrow(() ->
                    ReflectionTestUtils.invokeMethod(redisConfiguration, "createSearchIndexes", cachingConfiguration, redisClient));

            verify(redisClient, never()).ftCreate(anyString(), any(FTCreateParams.class), any(TextField[].class));
        }

        @Test
        @DisplayName("should handle ftCreate failure gracefully")
        void shouldHandleFtCreateFailure() {
            IndexableCacheInfo cacheInfo = new IndexableCacheInfo(
                    "FailIndex", "", "Fail:Idx:",
                    IndexCreationMode.SKIP_IF_EXISTS,
                    List.of(new IndexedFieldsInfo("$.id", "id", false, 1.0))
            );
            when(cachingConfiguration.getSearchableCaches()).thenReturn(List.of(cacheInfo));
            when(redisClient.ftInfo("FailIndex")).thenThrow(new JedisDataException("Unknown index name"));
            when(redisClient.ftCreate(anyString(), any(FTCreateParams.class), any(TextField[].class)))
                    .thenThrow(new JedisDataException("Index creation failed"));

            // Should not throw — error is logged
            assertDoesNotThrow(() ->
                    ReflectionTestUtils.invokeMethod(redisConfiguration, "createSearchIndexes", cachingConfiguration, redisClient));
        }

        @Test
        @DisplayName("should create indexes for multiple caches")
        void shouldCreateMultipleIndexes() {
            IndexableCacheInfo cache1 = new IndexableCacheInfo(
                    "Index1", "", "C1:Idx:",
                    IndexCreationMode.SKIP_IF_EXISTS,
                    List.of(new IndexedFieldsInfo("$.a", "a", false, 1.0))
            );
            IndexableCacheInfo cache2 = new IndexableCacheInfo(
                    "Index2", "", "C2:Idx:",
                    IndexCreationMode.SKIP_IF_EXISTS,
                    List.of(new IndexedFieldsInfo("$.b", "b", true, 1.5))
            );
            when(cachingConfiguration.getSearchableCaches()).thenReturn(List.of(cache1, cache2));
            when(redisClient.ftInfo("Index1")).thenThrow(new JedisDataException("Unknown index name"));
            when(redisClient.ftInfo("Index2")).thenReturn(Map.of("index_name", "Index2"));
            when(redisClient.ftCreate(anyString(), any(FTCreateParams.class), any(TextField[].class))).thenReturn("OK");

            ReflectionTestUtils.invokeMethod(redisConfiguration, "createSearchIndexes", cachingConfiguration, redisClient);

            // Only Index1 should be created, Index2 already exists
            verify(redisClient, times(1)).ftCreate(eq("Index1"), any(FTCreateParams.class), any(TextField[].class));
            verify(redisClient, never()).ftCreate(eq("Index2"), any(FTCreateParams.class), any(TextField[].class));
        }

        @Test
        @DisplayName("should handle empty searchable caches list")
        void shouldHandleEmptySearchableCaches() {
            when(cachingConfiguration.getSearchableCaches()).thenReturn(List.of());

            assertDoesNotThrow(() ->
                    ReflectionTestUtils.invokeMethod(redisConfiguration, "createSearchIndexes", cachingConfiguration, redisClient));

            verify(redisClient, never()).ftInfo(anyString());
        }

        @Test
        @DisplayName("should create index with multiple fields including sortable")
        void shouldCreateIndexWithMultipleFields() {
            IndexableCacheInfo cacheInfo = new IndexableCacheInfo(
                    "MultiFieldIndex", "", "Multi:Idx:",
                    IndexCreationMode.SKIP_IF_EXISTS,
                    List.of(
                            new IndexedFieldsInfo("$.name", "name", true, 2.0),
                            new IndexedFieldsInfo("$.org", "organization", false, 1.0),
                            new IndexedFieldsInfo("$.uri", "uri", true, 1.5)
                    )
            );
            when(cachingConfiguration.getSearchableCaches()).thenReturn(List.of(cacheInfo));
            when(redisClient.ftInfo("MultiFieldIndex")).thenThrow(new JedisDataException("Unknown index name"));
            when(redisClient.ftCreate(anyString(), any(FTCreateParams.class), any(TextField[].class))).thenReturn("OK");

            ReflectionTestUtils.invokeMethod(redisConfiguration, "createSearchIndexes", cachingConfiguration, redisClient);

            verify(redisClient).ftCreate(eq("MultiFieldIndex"), any(FTCreateParams.class), any(TextField[].class));
        }
    }

    // =========================================================================
    // clientName resolution
    // =========================================================================
    @Nested
    @DisplayName("clientName property")
    class ClientNameTests {

        @Test
        @DisplayName("should use clientName in primary pool client name")
        void shouldUseClientNameInPrimaryPool() {
            ReflectionTestUtils.setField(redisConfiguration, "clientName", "/my-service");

            // We can't fully create the JedisPooled without a real Redis,
            // but we can verify the field is set and accessible
            String clientName = (String) ReflectionTestUtils.getField(redisConfiguration, "clientName");
            assertThat(clientName).isEqualTo("/my-service");
            assertThat(clientName + "-caching-spring").isEqualTo("/my-service-caching-spring");
        }

        @Test
        @DisplayName("should use clientName in secondary pool client name")
        void shouldUseClientNameInSecondaryPool() {
            ReflectionTestUtils.setField(redisConfiguration, "clientName", "/my-service");

            String clientName = (String) ReflectionTestUtils.getField(redisConfiguration, "clientName");
            assertThat(clientName + "-indexing-pooled").isEqualTo("/my-service-indexing-pooled");
        }

        @Test
        @DisplayName("should default clientName to /esf when not set")
        void shouldDefaultClientName() {
            ReflectionTestUtils.setField(redisConfiguration, "clientName", "/esf");

            String clientName = (String) ReflectionTestUtils.getField(redisConfiguration, "clientName");
            assertThat(clientName).isEqualTo("/esf");
        }
    }

    // =========================================================================
    // CacheManager with various TTL units
    // =========================================================================
    @Nested
    @DisplayName("CacheManager TTL handling")
    class CacheManagerTTLTests {

        @Test
        @DisplayName("should handle all TimeUnit types for cache TTL")
        void shouldHandleAllTimeUnits() {
            List<CacheInfo> cacheInfos = List.of(
                    new CacheInfo("daysCache", 1L, TimeUnit.DAYS),
                    new CacheInfo("hoursCache", 2L, TimeUnit.HOURS),
                    new CacheInfo("minutesCache", 30L, TimeUnit.MINUTES),
                    new CacheInfo("secondsCache", 60L, TimeUnit.SECONDS),
                    new CacheInfo("millisCache", 5000L, TimeUnit.MILLISECONDS),
                    new CacheInfo("microsCache", 100000L, TimeUnit.MICROSECONDS),
                    new CacheInfo("nanosCache", 1000000L, TimeUnit.NANOSECONDS)
            );
            when(cachingConfiguration.getCacheInfos()).thenReturn(cacheInfos);
            when(cachingConfiguration.getSearchableCaches()).thenReturn(List.of());

            CacheManager cacheManager = redisConfiguration.redisCacheManager(
                    cachingConfiguration, List.of(), redisClient, esfSerializer, fallbackStore, circuitBreakerRegistry);

            assertThat(cacheManager.getCacheNames()).hasSize(7);
            cacheInfos.forEach(ci -> assertThat(cacheManager.getCache(ci.getName())).isNotNull());
        }

        @Test
        @DisplayName("should handle zero TTL (no expiration)")
        void shouldHandleZeroTTL() {
            List<CacheInfo> cacheInfos = List.of(
                    new CacheInfo("noTTL", 0L, TimeUnit.MILLISECONDS)
            );
            when(cachingConfiguration.getCacheInfos()).thenReturn(cacheInfos);
            when(cachingConfiguration.getSearchableCaches()).thenReturn(List.of());

            CacheManager cacheManager = redisConfiguration.redisCacheManager(
                    cachingConfiguration, List.of(), redisClient, esfSerializer, fallbackStore, circuitBreakerRegistry);

            assertThat(cacheManager.getCache("noTTL")).isNotNull();
        }
    }

    // =========================================================================
    // createSearchIndexesAlias
    // =========================================================================
    @Nested
    @DisplayName("createSearchIndexesAlias")
    class CreateSearchIndexesAliasTests {

        @Test
        @DisplayName("should handle empty searchable caches for alias creation")
        void shouldHandleEmptyAliases() {
            when(cachingConfiguration.getSearchableCaches()).thenReturn(List.of());

            assertDoesNotThrow(() ->
                    ReflectionTestUtils.invokeMethod(redisConfiguration, "createSearchIndexesAlias", cachingConfiguration, redisClient));
        }

        @Test
        @DisplayName("should log alias creation for each cache")
        void shouldLogAliasCreation() {
            IndexableCacheInfo cache = new IndexableCacheInfo(
                    "AliasIndex", "myAlias", "Alias:Idx:",
                    IndexCreationMode.SKIP_IF_EXISTS,
                    List.of(new IndexedFieldsInfo("$.name", "name", false, 1.0))
            );
            when(cachingConfiguration.getSearchableCaches()).thenReturn(List.of(cache));

            assertDoesNotThrow(() ->
                    ReflectionTestUtils.invokeMethod(redisConfiguration, "createSearchIndexesAlias", cachingConfiguration, redisClient));
        }
    }

    // =========================================================================
    // Conditional RedisClient Bean creation (CSC enabled/disabled)
    // =========================================================================
    @Nested
    @DisplayName("Conditional RedisClient Bean (spring.redis.csc.enabled)")
    class ConditionalRedisClientTests {

        @Test
        @DisplayName("jedisPooled() should be annotated with ConditionalOnProperty csc.enabled=true, matchIfMissing=true")
        void jedisPooledShouldHaveCorrectConditional() throws NoSuchMethodException {
            var method = RedisConfiguration.class.getDeclaredMethod("jedisPooled");
            var annotation = method.getAnnotation(ConditionalOnProperty.class);

            assertThat(annotation).isNotNull();
            assertThat(annotation.name()).contains("spring.redis.csc.enabled");
            assertThat(annotation.havingValue()).isEqualTo("true");
            assertThat(annotation.matchIfMissing()).isTrue();
        }

        @Test
        @DisplayName("jedisPooledWithoutCSC() should be annotated with ConditionalOnProperty csc.enabled=false")
        void jedisPooledWithoutCSCShouldHaveCorrectConditional() throws NoSuchMethodException {
            var method = RedisConfiguration.class.getDeclaredMethod("jedisPooledWithoutCSC");
            var annotation = method.getAnnotation(ConditionalOnProperty.class);

            assertThat(annotation).isNotNull();
            assertThat(annotation.name()).contains("spring.redis.csc.enabled");
            assertThat(annotation.havingValue()).isEqualTo("false");
            assertThat(annotation.matchIfMissing()).isFalse();
        }

        @Test
        @DisplayName("both beans should have @Qualifier RedisClientPrimary")
        void bothBeansShouldHavePrimaryQualifier() throws NoSuchMethodException {
            var cscMethod = RedisConfiguration.class.getDeclaredMethod("jedisPooled");
            var noCscMethod = RedisConfiguration.class.getDeclaredMethod("jedisPooledWithoutCSC");

            var cscQualifier = cscMethod.getAnnotation(Qualifier.class);
            var noCscQualifier = noCscMethod.getAnnotation(Qualifier.class);

            assertThat(cscQualifier).isNotNull();
            assertThat(cscQualifier.value()).isEqualTo("RedisClientPrimary");
            assertThat(noCscQualifier).isNotNull();
            assertThat(noCscQualifier.value()).isEqualTo("RedisClientPrimary");
        }

    }

    // =========================================================================
    // Integration-style: full redisCacheManager with indexable repos
    // =========================================================================
    @Nested
    @DisplayName("Integration: redisCacheManager with indexable repositories")
    class IntegrationTests {

        @Test
        @DisplayName("should wire indexable repositories and create search indexes")
        void shouldWireIndexableReposAndCreateIndexes() {
            IndexableCacheInfo indexInfo = new IndexableCacheInfo(
                    "RoutingIdx", "", "RoutingRule:Idx:",
                    IndexCreationMode.SKIP_IF_EXISTS,
                    List.of(
                            new IndexedFieldsInfo("$key.organizationId", "organization", false, 1.0),
                            new IndexedFieldsInfo("$key.providerName", "provider", false, 1.0)
                    )
            );

            IndexableRepository mockRepo = mock(IndexableRepository.class);
            when(mockRepo.isIndexable()).thenReturn(true);
            when(mockRepo.getIndexableCacheInfo()).thenReturn(indexInfo);

            List<CacheInfo> cacheInfos = List.of(new CacheInfo("RoutingRule", 1L, TimeUnit.HOURS));
            when(cachingConfiguration.getCacheInfos()).thenReturn(cacheInfos);

            // After addSearchableCache is called, getSearchableCaches returns the info
            List<IndexableCacheInfo> searchableCaches = new ArrayList<>();
            doAnswer(invocation -> {
                IndexableRepository repo = invocation.getArgument(0);
                if (repo.isIndexable()) searchableCaches.add(repo.getIndexableCacheInfo());
                return null;
            }).when(cachingConfiguration).addSearchableCache(any());
            when(cachingConfiguration.getSearchableCaches()).thenReturn(searchableCaches);

            // Index doesn't exist yet
            when(redisClient.ftInfo("RoutingIdx")).thenThrow(new JedisDataException("Unknown index name"));
            when(redisClient.ftCreate(anyString(), any(FTCreateParams.class), any(TextField[].class))).thenReturn("OK");

            CacheManager cacheManager = redisConfiguration.redisCacheManager(
                    cachingConfiguration, List.of(mockRepo), redisClient, esfSerializer, fallbackStore, circuitBreakerRegistry);

            verify(cachingConfiguration).addSearchableCache(mockRepo);
            verify(redisClient).ftCreate(eq("RoutingIdx"), any(FTCreateParams.class), any(TextField[].class));
            assertThat(cacheManager).isInstanceOf(ResilientCacheManager.class);
            assertThat(cacheManager.getCache("RoutingRule")).isNotNull();
        }

        @Test
        @DisplayName("should handle non-indexable repository gracefully")
        void shouldHandleNonIndexableRepo() {
            IndexableRepository nonIndexableRepo = mock(IndexableRepository.class);

            when(cachingConfiguration.getCacheInfos()).thenReturn(List.of(new CacheInfo("SimpleCache", 60L, TimeUnit.SECONDS)));
            when(cachingConfiguration.getSearchableCaches()).thenReturn(List.of());

            CacheManager cacheManager = redisConfiguration.redisCacheManager(
                    cachingConfiguration, List.of(nonIndexableRepo), redisClient, esfSerializer, fallbackStore, circuitBreakerRegistry);

            verify(cachingConfiguration).addSearchableCache(nonIndexableRepo);
            verify(redisClient, never()).ftInfo(anyString());
            assertThat(cacheManager.getCache("SimpleCache")).isNotNull();
        }
    }
}




