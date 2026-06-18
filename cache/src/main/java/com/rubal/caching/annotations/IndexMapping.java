package com.rubal.caching.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.TYPE)
public @interface IndexMapping {

    /** (Mandatory) The name of the index. */
    String suffix() default "Idx";

    /** (Optional) The alias of the index. */
    String indexAlias() default "";

    IndexCreationMode mode() default IndexCreationMode.SKIP_IF_EXISTS;

    /** (Required) Array of <code>IndexFieldMap</code> annotations. */
    IndexFieldMap[] fields() default {};

}
