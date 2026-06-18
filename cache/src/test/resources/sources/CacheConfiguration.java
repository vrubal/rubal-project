package com.fiserv.bsol.esf.caching.annotations.sources;

import com.rubal.caching.annotations.CacheDefinition;
import com.rubal.caching.annotations.CacheEnable;
import com.rubal.caching.samplesources.RoutingInquiry;
import com.rubal.caching.samplesources.RoutingRules;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@SuppressWarnings({"unused", "WeakerAccess"})
@Configuration
@ComponentScan(basePackages = "com.fiserv.bsol.esf.caching")
@CacheEnable(caches = {
        @CacheDefinition(name = "RoutingRule", key = RoutingInquiry.class, value = RoutingRules.class)
})
public class CacheConfiguration {

}