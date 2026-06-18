package com.rubal.caching.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.concurrent.TimeUnit;

/**
 * caching definition contains name, key, and value.
 * <ul>
 * <li>name: Name of the Cache</li>
 * <li>key:  The storage key</li>
 * <li>keyindex: The indexes of this key</li>
 * <li>value: The value to be stored</li>
 * <li>valindex: The index of this value</li>
 * <li>ttl: The time to live DAYS/HOURS/MINUTES/SECONDS/MILLISECONDS/MICROSECONDS/NANOSECONDS</li>
 * </ul>
 */
@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.TYPE)
public @interface CacheDefinition {
    /**
     * @return the name of the cache
     */
    String name();

    /**
     * @return the class of the key
     */
    Class<?> key();

    /**
     * @return the class of the value
     */
    Class<?> value();

    /**
     * @return the time to live value
     */
    long ttl() default 0L;

    /**
     * @return the unit of the time to live
     */
    TimeUnit unit() default TimeUnit.MILLISECONDS;

    /**
     * Defines if index will be built for this cache
     */
    boolean indexable() default false;
    /**
     * Defines which values are indexable
     */
    IndexMapping mapping() default @IndexMapping(mode = IndexCreationMode.SKIP);
}