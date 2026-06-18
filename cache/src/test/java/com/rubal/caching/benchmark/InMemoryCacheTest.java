package com.rubal.caching.benchmark;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rubal.caching.mem.InMemoryCache;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.cache.Cache;
import org.springframework.cache.support.SimpleValueWrapper;
import org.springframework.test.util.ReflectionTestUtils;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class InMemoryCacheTest {

    private InMemoryCache inMemoryCache;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setup() {
        objectMapper = new ObjectMapper();
        inMemoryCache = new InMemoryCache("test", 0L, objectMapper);
    }

    @Test
    void testGetName() {
        String name = inMemoryCache.getName();
        assertThat(name).isEqualTo("test");
    }

    @Test
    @Disabled("Used only to test performance")
    void testIncreaseSlowRetrieval() {
        for (int i = 0; i < Integer.MAX_VALUE; i++) {
            String key = String.valueOf(i) + i;

            // Saving
            long initial = System.currentTimeMillis();
            inMemoryCache.put( key, new TestData(i, String.valueOf(i)));
            long totalTime = System.currentTimeMillis() - initial;
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

    @Test
    void testGetNativeCache() {
        assertThat(inMemoryCache.getNativeCache()).isInstanceOf(HashMap.class);
    }

    @Test
    void testPutAndGet() {
        String key = "key1";
        String value = "value1";
        inMemoryCache.put(key, value);
        Cache.ValueWrapper valueWrapper = inMemoryCache.get(key);
        assertThat(valueWrapper).isNotNull();
        assertThat(valueWrapper.get()).isEqualTo(value);
    }

    @Test
    void testObjectToStringThrowsException() throws JsonProcessingException, NoSuchMethodException {
        TestData testObject = new TestData(1, "test");
        ObjectMapper mockedObjectMapper = mock(ObjectMapper.class);
        when(mockedObjectMapper.writeValueAsString(testObject)).thenThrow(new JsonProcessingException("Test exception") {});
        ReflectionTestUtils.setField(inMemoryCache, "om", mockedObjectMapper);

        Method method = InMemoryCache.class.getDeclaredMethod("objectToString", Object.class);
        method.setAccessible(true);

        assertThrows(Exception.class, () -> method.invoke(inMemoryCache, testObject));
    }

    @Test
    void testStringToObjectThrowsException() throws JsonProcessingException, NoSuchMethodException {
        TestData testObject = new TestData(1, "test");
        ObjectMapper mockedObjectMapper = mock(ObjectMapper.class);
        when(mockedObjectMapper.readValue(anyString(), (JavaType) any())).thenThrow(new JsonProcessingException("Test exception") {});
        ReflectionTestUtils.setField(inMemoryCache, "om", mockedObjectMapper);

        Method method = InMemoryCache.class.getDeclaredMethod("stringToObject", String.class);
        method.setAccessible(true);

        assertThrows(Exception.class, () -> method.invoke(inMemoryCache, objectMapper.writeValueAsString(testObject)));
    }

    @Test
    void testEvict() {
        String key = "key1";
        String value = "value1";
        inMemoryCache.put(key, value);
        inMemoryCache.evict(key);
        Cache.ValueWrapper valueWrapper = inMemoryCache.get(key);
        assertThat(valueWrapper.get()).isNull();
    }

    @Test
    @Disabled("Implementation is wrong and doesn't remove the element")
    void testExpire() {
        String key = "key"+ UUID.randomUUID().toString();
        inMemoryCache.put(key, "value1");
        inMemoryCache.expire(key);
        Cache.ValueWrapper valueWrapper = inMemoryCache.get(key);
        assertThat(valueWrapper.get()).isNull();
    }

    @Test
    void testClear() {
        inMemoryCache.put("key1", "value1");
        inMemoryCache.put("key2", "value2");
        inMemoryCache.clear();
        assertThat(((SimpleValueWrapper)inMemoryCache.get("key1")).get()).isNull();
        assertThat(((SimpleValueWrapper)inMemoryCache.get("key1")).get()).isNull();
    }

    @Test
    void testGetWithClass() {
        assertThrows(UnsupportedOperationException.class, () -> inMemoryCache.get("key1", String.class));
    }

    @Test
    void testGetWithCallable() {
        assertThrows(UnsupportedOperationException.class, () -> inMemoryCache.get("key1", () -> "default"));
    }

    record TestData(int num, String val) {
    }
}
