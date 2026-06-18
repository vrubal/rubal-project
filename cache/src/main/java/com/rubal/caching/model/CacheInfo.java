package com.rubal.caching.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.concurrent.TimeUnit;

/**
 *  The Cache Info object
 */
@Data
@AllArgsConstructor
public class CacheInfo {
    /**
     *  the name of the cache
     */
    private String name;
    /**
     * the time to live of the cache
     */
    private Long ttl;
    /**
     * the unit of the ttl
     */
    private TimeUnit unit;
}
