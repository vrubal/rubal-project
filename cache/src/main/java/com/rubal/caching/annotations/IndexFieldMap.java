package com.rubal.caching.annotations;

import com.rubal.caching.redis.FieldType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

//@Repeatable(IndexMapping.class)
@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.TYPE)
public @interface IndexFieldMap {

    String path() default "";

    String alias() default "";

    FieldType type() default FieldType.TEXT;

    boolean sortable() default false;

    double weight() default 1.0;

}
