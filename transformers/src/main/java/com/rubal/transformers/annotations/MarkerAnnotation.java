package com.rubal.transformers.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

//MarkerAnnotation without the variables/values
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.CLASS)
public @interface MarkerAnnotation {
}
