package com.rubal.transformers.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.SOURCE)
public @interface SmartPhone {
    String os() default "Symbian";
    String version() default  "1.0";
}

@SmartPhone(os="Symbian", version="1.2")
class NokiaSeries{

}
