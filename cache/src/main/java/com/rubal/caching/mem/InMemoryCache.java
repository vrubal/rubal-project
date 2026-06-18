package com.rubal.caching.mem;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rubal.caching.model.CacheObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.Cache;
import org.springframework.cache.support.SimpleValueWrapper;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;

/**
 * in memory cache
 */
public class InMemoryCache implements Cache {

    private static final Logger logger = LoggerFactory.getLogger(InMemoryCache.class);
    /**
     * object mapper
     */
    private final ObjectMapper om;
    /**
     * name of the cache
     */
    private final String name;
    /**
     * hashmap of key, value entries
     */
    private final Map<String, String> elements = new HashMap<>();
    /**
     * the ttl of this cache
     */
    private final long ttl;

    /**
     * instantiate in memory cache using provided name
     *
     * @param name         what cache is called
     * @param objectMapper the object mapper that serialize the objects
     */
    public InMemoryCache(String name, long ttl, ObjectMapper objectMapper) {
        this.name = name;
        this.om = objectMapper;
        this.ttl = ttl;
    }

    /**
     * expire a key from the cache
     *
     * @param key the key to expire
     */
    @Deprecated(since = "Needs to be re-worked")
    public void expire(String key) {
        synchronized (elements) {
            elements.remove(key);
        }
    }

    /**
     * convert object to string
     *
     * @param o object
     * @return string representation of object
     */
    private String objectToString(Object o) {
        try {
            CacheObject co = new CacheObject(om.writeValueAsString(o), o.getClass());
            return om.writeValueAsString(co);
        } catch (JsonProcessingException je) {
            throw new UnsupportedOperationException("json write error " + je.getMessage());
        }
    }

    /**
     * convert string to object
     *
     * @param str string provided
     * @return object created from string
     */
    private Object stringToObject(String str) {
        if (str == null) return null;
        try {
            CacheObject co = om.readValue(str, CacheObject.class);
            return om.readValue(co.jsonObject(), co.clazz());
        } catch (JsonProcessingException je) {
            throw new UnsupportedOperationException("json read error " + je.getMessage());
        }
    }

    /**
     * name of cache
     *
     * @return cache name
     */
    @Override
    public String getName() {
        return name;
    }

    /**
     * native cache
     *
     * @return native cache object
     */
    @Override
    public Object getNativeCache() {
        return elements;
    }

    /**
     * value wrapper of the object
     *
     * @param o object provided
     * @return value wrapper of object
     */
    @Override
    public ValueWrapper get(Object o) {
        String val = null;
        synchronized (elements) {
            val = elements.get(objectToString(o));
        }
        return new SimpleValueWrapper(stringToObject(val));
    }

    /**
     * get object for provided class type, unsupported
     *
     * @param o      object
     * @param aClass class
     * @param <T>    type of object
     * @return type of object
     */
    @Override
    public <T> T get(Object o, Class<T> aClass) {
        throw new UnsupportedOperationException();
    }

    /**
     * get object for callable, unsupported
     *
     * @param o        object provided
     * @param callable callable object
     * @param <T>      type of object
     * @return type of object
     */
    @Override
    public <T> T get(Object o, Callable<T> callable) {
        throw new UnsupportedOperationException();
    }

    /**
     * put the object key, value into the cache
     *
     * @param o  key object
     * @param o1 value object
     */
    @Override
    public void put(Object o, Object o1) {
        String key = objectToString(o);
        synchronized (elements) {
            elements.put(key, objectToString(o1));
        }
        setTtl(key);
    }

    /**
     * set the ttl of this key using the thread
     *
     * @param key the key for this ttl
     */
    private void setTtl(String key) {
        if (ttl > 0) {
            logger.debug("TTL feature for in memory caching has been disabled");
        }
    }

    /**
     * evice object from cache
     *
     * @param o object provided
     */
    @Override
    public void evict(Object o) {
        synchronized (elements) {
            elements.remove(objectToString(o));
        }

    }

    /**
     * clear all objects from cache
     */
    @Override
    public void clear() {
        synchronized (elements) {
            elements.clear();
        }
    }
}
