package com.rubal.caching.model;

/**
 * cache object containing string and class
 */
public record CacheObject(String jsonObject, Class<?> clazz) {
}
