package com.rubal.caching.config;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.BasicPolymorphicTypeValidator;
import com.fasterxml.jackson.databind.jsontype.PolymorphicTypeValidator;
import com.rubal.caching.indexing.IndexableRepository;
import com.rubal.caching.model.IndexableCacheInfo;
import com.rubal.caching.model.IndexedFieldsInfo;
import com.rubal.caching.redis.ESFSerializer;
import com.rubal.caching.redis.JedisCSCCacheManager;
import com.rubal.caching.resilient.FallbackStore;
import com.rubal.caching.resilient.ResilientCacheManager;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import io.github.resilience4j.common.circuitbreaker.configuration.CircuitBreakerConfigCustomizer;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import redis.clients.jedis.*;
import redis.clients.jedis.csc.CacheConfig;
import redis.clients.jedis.csc.Cacheable;
import redis.clients.jedis.csc.DefaultCacheable;
import redis.clients.jedis.exceptions.JedisDataException;
import redis.clients.jedis.search.FTCreateParams;
import redis.clients.jedis.search.IndexDataType;
import redis.clients.jedis.search.schemafields.TextField;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;

@Configuration
@ConditionalOnProperty(value = "spring.cache.type", havingValue = "redis")
public class RedisConfiguration implements CachingConfigurer {

    private static final Logger logger = LoggerFactory.getLogger(RedisConfiguration.class);

    /**
     * the redis host
     */
    @Value("${spring.redis.host}")
    private String host;

    /**
     * the redis port
     */
    @Value("${spring.redis.port:10500}")
    private int port;

    /**
     * the redis password
     */
    @Value("${spring.redis.password}")
    private String pwd;

    /**
     * Maximum time allowed for reading or writing data while executing a command
     */
    @Value("${spring.redis.timeout:5000}")
    private int timeout;

    /**
     * Maximum time allowed for establishing a new connection.
     */
    @Value("${spring.redis.connect-timeout:60000}")
    private int connectTimeout;

    @Value("${spring.redis.database:0}")
    private int database;

    /*
     * Application service client connection identifier in Redis
     *  - Caching pool will have as name {application.name}-caching-spring
     *  - Indexing pool will have as name {application.name}-indexing
     */
    private String clientName;

    /**
     * Maximum number of active connections that can be allocated.
     */
    @Value("${spring.redis.jedis.pool.max-active:4}")
    private int poolMaxActive;

    /**
     * Maximum number of connections that can remain idle in the pool.
     */
    @Value("${spring.redis.jedis.pool.max-idle:4}")
    private int poolMaxIdle;

    /**
     * Minimum number of connections that can remain idle in the pool.
     */
    @Value("${spring.redis.jedis.pool.min-idle:2}")
    private int poolMinIdle;

    /**
     * Enables sending a PING command periodically while the connection is idle.
     * This helps to ensure that connections are still alive and can help to prevent issues with stale connections in the pool.
     */
    @Value("${spring.redis.jedis.pool.test-while-idle:true}")
    private boolean poolTestWhileIdle;

    /**
     * Controls the period between checks for idle connections in the pool. When negative, no idle connection eviction is performed.
     */
    @Value("${spring.redis.jedis.pool.time-between-eviction-runs:15000}")
    private long timeBetweenEvictionRuns;

    /**
     * Determines the behavior when the pool is exhausted.
     * If true, the pool will block and wait for a connection to become available.
     * If false, it will throw an exception immediately.
     */
    @Value("${spring.redis.jedis.pool.block-when-exhausted:true}")
    private boolean blockWhenExhausted;

    /**
     * Maximum time to wait for a connection from the pool when the pool is exhausted and blockWhenExhausted is true.
     * When the time expires, an exception will be thrown. A negative value means to wait indefinitely.
     */
    @Value("${spring.redis.jedis.pool.max-wait:1000}")
    private long maxWait;

    @Value("${spring.redis.csc.exclude.key-prefixes:}")
    private List<String> cscKeyPrefixes;

    @Value("${spring.redis.csc.max-size:10000}")
    private int cscMaxSize;

    /**
     * Maximum number of entries retained in the shared in-memory fallback cache used when Redis is
     * down.
     */
    @Value("${spring.redis.resilient.fallback.max-size:50000}")
    private long resilientFallbackMaxSize;

    /**
     * Time-to-live (in seconds) for in-memory fallback entries. {@code 0} disables TTL so entries
     * are bounded only by {@code max-size} and are wiped when Redis recovers.
     */
    @Value("${spring.redis.resilient.fallback.ttl-seconds:0}")
    private long resilientFallbackTtlSeconds;

    /**
     * Failure-rate percentage at which the {@code redisCache} circuit opens.
     */
    @Value("${spring.redis.resilient.circuit-breaker.failure-rate-threshold:50}")
    private float cbFailureRateThreshold;

    /**
     * Number of calls recorded before the failure rate is evaluated.
     */
    @Value("${spring.redis.resilient.circuit-breaker.sliding-window-size:20}")
    private int cbSlidingWindowSize;

    /**
     * Minimum number of calls required before the circuit can open.
     */
    @Value("${spring.redis.resilient.circuit-breaker.minimum-number-of-calls:10}")
    private int cbMinimumNumberOfCalls;

    /**
     * How long (in seconds) the circuit stays open before transitioning to half-open to probe Redis.
     */
    @Value("${spring.redis.resilient.circuit-breaker.wait-duration-open-seconds:10}")
    private long cbWaitDurationOpenSeconds;

    /**
     * Number of trial calls permitted while the circuit is half-open.
     */
    @Value("${spring.redis.resilient.circuit-breaker.permitted-calls-half-open:5}")
    private int cbPermittedCallsHalfOpen;

    public RedisConfiguration(Environment environment) {
        //clientName = ApplicationNameIdentifier.applicationName(environment);
    }

    /**
     * Primary Redis-backed cache manager, decorated with an in-memory fallback so caching keeps
     * working when Redis is unavailable.
     * <p>
     * Cache operations normally go through a CSC-enabled {@link JedisPooled}, which transparently
     * caches responses locally with server-assisted invalidation via {@code CLIENT TRACKING}. The
     * {@link ResilientCacheManager} wrapper guards Redis access with a circuit breaker, mirrors
     * entries into a Caffeine-backed {@link FallbackStore} while Redis is healthy, serves them when
     * the circuit is open, and wipes the mirror once the circuit closes.
     *
     * @param cachingConfiguration cache configuration
     * @return resilient cache manager backed by Jedis CSC with in-memory fallback
     */
    @Bean
    @Primary
    public CacheManager redisCacheManager(CachingConfiguration cachingConfiguration, List<IndexableRepository> indexableRepositories,
                                          @Qualifier("RedisClientPrimary") RedisClient redisClient, ESFSerializer esfSerializer,
                                          FallbackStore fallbackStore, CircuitBreakerRegistry circuitBreakerRegistry) {

        indexableRepositories.forEach(cachingConfiguration::addSearchableCache);
        createSearchIndexes(cachingConfiguration, redisClient);

        JedisCSCCacheManager redisBackedCacheManager =
                new JedisCSCCacheManager(redisClient, esfSerializer, cachingConfiguration);

        return new ResilientCacheManager(redisBackedCacheManager, fallbackStore, circuitBreakerRegistry);
    }

    /**
     * Single shared in-memory fallback store used while Redis is unavailable. Backed by a bounded
     * Caffeine cache so total fallback memory has one global ceiling and recovery wipes are atomic.
     *
     * @return the fallback store
     */
    @Bean
    public FallbackStore fallbackStore() {
        logger.info("Resilient cache enabled (fallback max-size={}, ttl={}s, circuit='{}').",
                resilientFallbackMaxSize, resilientFallbackTtlSeconds, ResilientCacheManager.CIRCUIT_BREAKER_NAME);
        return new FallbackStore(resilientFallbackMaxSize, resilientFallbackTtlSeconds);
    }

    /**
     * Library-provided {@link CircuitBreakerRegistry} guarding Redis access.
     * <p>
     * Defining the registry here makes the caching library self-contained: it no longer depends on
     * Resilience4j's Spring Boot auto-configuration being present (or enabled) in the consuming
     * application. The breaker config is built from the {@code spring.redis.resilient.circuit-breaker.*}
     * properties and installed as the registry's default configuration so the single shared
     * {@code redisCache} breaker resolved by {@link ResilientCacheManager} uses it.
     * <p>
     * Marked {@link ConditionalOnMissingBean} so an application that already exposes its own
     * {@code CircuitBreakerRegistry} (for example via the Resilience4j starter) keeps full control.
     *
     * @return the circuit-breaker registry for the {@code redisCache} breaker
     */
    @Bean
    @ConditionalOnMissingBean(CircuitBreakerRegistry.class)
    public CircuitBreakerRegistry circuitBreakerRegistry() {
        CircuitBreakerConfig redisCacheConfig = CircuitBreakerConfig.custom()
                .failureRateThreshold(cbFailureRateThreshold)
                .slidingWindowType(CircuitBreakerConfig.SlidingWindowType.COUNT_BASED)
                .slidingWindowSize(cbSlidingWindowSize)
                .minimumNumberOfCalls(cbMinimumNumberOfCalls)
                .waitDurationInOpenState(Duration.ofSeconds(cbWaitDurationOpenSeconds))
                .permittedNumberOfCallsInHalfOpenState(cbPermittedCallsHalfOpen)
                .automaticTransitionFromOpenToHalfOpenEnabled(true)
                .build();
        return CircuitBreakerRegistry.of(redisCacheConfig);
    }

    /**
     * Library-provided default configuration for the {@code redisCache} circuit breaker, applied when
     * the consuming application supplies its own {@link CircuitBreakerRegistry} via Resilience4j's
     * Spring Boot auto-configuration. Applications can still override any of these via
     * {@code resilience4j.circuitbreaker.instances.redisCache.*} properties, which take precedence
     * over this programmatic customizer.
     *
     * @return a circuit-breaker config customizer for the {@code redisCache} instance
     */
    @Bean
    public CircuitBreakerConfigCustomizer redisCacheCircuitBreakerCustomizer() {
        return CircuitBreakerConfigCustomizer.of(ResilientCacheManager.CIRCUIT_BREAKER_NAME, builder -> builder
                .failureRateThreshold(cbFailureRateThreshold)
                .slidingWindowType(CircuitBreakerConfig.SlidingWindowType.COUNT_BASED)
                .slidingWindowSize(cbSlidingWindowSize)
                .minimumNumberOfCalls(cbMinimumNumberOfCalls)
                .waitDurationInOpenState(Duration.ofSeconds(cbWaitDurationOpenSeconds))
                .permittedNumberOfCallsInHalfOpenState(cbPermittedCallsHalfOpen)
                .automaticTransitionFromOpenToHalfOpenEnabled(true));
    }

    @Bean
    public ESFSerializer esfSerializer() {
        ObjectMapper objectMapper = redisObjectMapper();
        return new ESFSerializer(objectMapper);
    }

    private ObjectMapper redisObjectMapper() {
        // redis mapper registering modules based on team reviews
        ObjectMapper redisObjectMapper = new ObjectMapper().findAndRegisterModules();

        // validator for base type Object based on team reviews
        PolymorphicTypeValidator ptv = BasicPolymorphicTypeValidator.builder()
                .allowIfBaseType(Object.class)
                .build();

        redisObjectMapper.activateDefaultTyping(ptv, ObjectMapper.DefaultTyping.NON_FINAL, JsonTypeInfo.As.PROPERTY);

        return redisObjectMapper;
    }

    /**
     * Creates a CSC-enabled JedisPooled instance with RESP3 and client-side caching.
     *
     * @return RedisClient with client-side caching enabled
     */
    @Bean
    @ConditionalOnProperty(name = "spring.redis.csc.enabled", havingValue = "true", matchIfMissing = true)
    @Qualifier("RedisClientPrimary")
    public RedisClient jedisPooled() {
        logger.info("======>LOG ENABLED jedisPooled with CSC-Primary {}:{}", host, port);
        CacheConfig cacheConfig = CacheConfig.builder()
                .maxSize(cscMaxSize)
                .cacheable(checkCacheable())
                .build();
        return buildPrimaryRedisClient(true, cacheConfig);
    }

    /**
     * Creates a JedisPooled instance without client-side caching.
     *
     * @return RedisClient with client-side caching disabled
     */
    @Bean
    @ConditionalOnProperty(name = "spring.redis.csc.enabled", havingValue = "false")
    @Qualifier("RedisClientPrimary")
    public RedisClient jedisPooledWithoutCSC() {
        logger.info("======>LOG ENABLED jedisPooled without CSC-Primary {}:{}", host, port);
        return buildPrimaryRedisClient(false, null);
    }

    private RedisClient buildPrimaryRedisClient(boolean cscEnabled, CacheConfig cacheConfig) {
        GenericObjectPoolConfig<Connection> configPool = buildPoolConfig();
        HostAndPort hostAndPort = new HostAndPort(host, port);
        String finalClientName = clientName + "-caching-spring";

        DefaultJedisClientConfig.Builder configBuilder = DefaultJedisClientConfig.builder()
                .clientName(finalClientName)
                .connectionTimeoutMillis(connectTimeout)
                .socketTimeoutMillis(timeout)
                .password(pwd);
        if (cscEnabled) {
            configBuilder.resp3();
        }

        var builder = new RedisClient.Builder()
                .hostAndPort(hostAndPort)
                .clientConfig(configBuilder.build())
                .poolConfig(configPool);
        if (cscEnabled && cacheConfig != null) {
            builder.cacheConfig(cacheConfig);
        }
        return builder.build();
    }

    @Bean
    @Qualifier("RedisClientSecondary")
    public RedisClient jedisPooledForIndex() {
        logger.info("======>LOG ENABLED jedisPooled with CSC-Secondary {}:{}", host, port);
        GenericObjectPoolConfig<Connection> configPool = buildPoolConfig();
        HostAndPort hostAndPort = new HostAndPort(host, port);
        String finalClientName = clientName + "-indexing-pooled";
        JedisClientConfig clientConfig = DefaultJedisClientConfig.builder()
                .clientName(finalClientName)
                .connectionTimeoutMillis(connectTimeout)
                .socketTimeoutMillis(timeout)
                .password(pwd)
                .build();
        return new RedisClient.Builder().
                hostAndPort(hostAndPort)
                .clientConfig(clientConfig)
                .poolConfig(configPool).build();
    }

    /**
     * Builds a {@link Cacheable} that filters which keys are eligible for client-side caching.
     * <p>
     * If {@code spring.redis.csc.key-prefixes} is set (comma-separated), only keys starting with
     * one of the specified prefixes will be cached locally. If empty/unset, all cacheable commands
     * are cached (default Jedis behavior).
     *
     * @return a Cacheable instance
     */
    private Cacheable checkCacheable() {
        if (cscKeyPrefixes == null || cscKeyPrefixes.isEmpty()) {
            return DefaultCacheable.INSTANCE;
        }
        return (command, keys) -> {
            if (!DefaultCacheable.INSTANCE.isCacheable(command, keys)) {
                return false;
            }
            if (keys == null || keys.isEmpty()) {
                return false;
            }
            for (Object keyObj : keys) {
                String key;
                if (keyObj instanceof byte[] bytes) {
                    key = new String(bytes);
                } else {
                    key = keyObj.toString();
                }
                for (String prefix : cscKeyPrefixes) {
                    if (key.startsWith(prefix)) {
                        return false;
                    }
                }
            }
            return true;
        };
    }

    /*
     * Builds the pool configuration
     *
     * @return pool configuration
     */
    private GenericObjectPoolConfig<Connection> buildPoolConfig() {
        GenericObjectPoolConfig<Connection> configPool = new GenericObjectPoolConfig<>();
        configPool.setMaxTotal(poolMaxActive);
        configPool.setMaxIdle(poolMaxIdle);
        configPool.setMinIdle(poolMinIdle);
        // Enables sending a PING command periodically while the connection is idle.
        configPool.setTestWhileIdle(poolTestWhileIdle);
        configPool.setTimeBetweenEvictionRuns(Duration.ofMillis(timeBetweenEvictionRuns));
        // Enables waiting for a connection to become available.
        configPool.setBlockWhenExhausted(blockWhenExhausted);
        long effectiveMaxWait = maxWait;
        if (effectiveMaxWait < -1) {
            logger.warn("Invalid maxWait value {}. Normalizing to -1 (indefinite).", effectiveMaxWait);
            effectiveMaxWait = -1;
        }
        configPool.setMaxWait(Duration.ofMillis(effectiveMaxWait));
        return configPool;
    }

    /**
     * Creates the search indexes
     */
    private void createSearchIndexes(CachingConfiguration cachingConfiguration, RedisClient redisClient) {
        List<IndexableCacheInfo> caches = cachingConfiguration.getSearchableCaches();

        for (IndexableCacheInfo cache : caches) {
            try {
                // Checks for existence of index
                redisClient.ftInfo(cache.name());
                logger.debug("Index {} already exists, skipping", cache.name());
            } catch (JedisDataException e) {
                if (e.getMessage().equalsIgnoreCase("Unknown index name")) {
                    // If index doesn't exist then create it
                    createSearchIndex(cache, redisClient);
                }
            }
        }
    }

    /**
     * Creates a search index
     *
     * @param cache
     * @param redisClient
     */
    private static void createSearchIndex(IndexableCacheInfo cache, RedisClient redisClient) {
        IndexedFieldsInfo[] indexProperties = new IndexedFieldsInfo[cache.indexableProperties().size()];
        cache.indexableProperties().toArray(indexProperties);

        TextField[] fields = Arrays.stream(indexProperties)
                .map(fieldInfo -> {
                    TextField schemaField = TextField.of(fieldInfo.path())
                            .as(fieldInfo.alias())
                            .weight(fieldInfo.weight());
                    if (fieldInfo.sortable()) schemaField.sortable();
                    return schemaField;
                }).toArray(TextField[]::new);

        try {
            redisClient.ftCreate(cache.name(), FTCreateParams.createParams().on(IndexDataType.JSON).prefix(cache.keyPrefix()), fields);
            logger.info("Created Search Index for cache: {}", cache.name());
        } catch (JedisDataException e) {
            logger.error("Unable to create index for cache {}: {}", cache.name(), e.getMessage());
        }
    }

    /**
     * Creates the search indexes alias
     */
    private void createSearchIndexesAlias(CachingConfiguration cachingConfiguration, RedisClient jedis) {
        List<IndexableCacheInfo> caches = cachingConfiguration.getSearchableCaches();

        for (IndexableCacheInfo cache : caches) {
            try {
                //TODO: Command not available in jedis
                //jedis.ftAliasadd(cache.indexAlias(), cache.name());
                logger.info("Added alias {} for Index: {}", cache.indexAlias(), cache.name());
            } catch (JedisDataException e) {
                logger.error("Unable to add alias to index: {}", e.getMessage());
            }
        }
    }

}
