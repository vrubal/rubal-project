package com.rubal.caching.config;

import com.rubal.caching.annotations.CacheDefinition;
import com.rubal.caching.annotations.CacheEnable;
import com.rubal.caching.annotations.IndexFieldMap;
import com.rubal.caching.annotations.IndexMapping;
import com.rubal.caching.samplesources.RoutingInquiry;
import com.rubal.caching.samplesources.RoutingRules;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@SuppressWarnings({"unused", "WeakerAccess"})
@Configuration
@ComponentScan(basePackages = "com.fiserv.bsol.esf.caching")
@CacheEnable(caches = {
        @CacheDefinition(name = "RoutingRule", key = RoutingInquiry.class, value = RoutingRules.class, indexable = true,
                mapping = @IndexMapping(fields = {
                        @IndexFieldMap(path = "$key.organizationId", alias = "organization"),
                        @IndexFieldMap(path = "$key.providerName", alias = "provider"),
                        @IndexFieldMap(path = "$key.httpMethodURI", alias = "uri"),
                        @IndexFieldMap(path = "$key.httpVerb", alias = "verb")
                }))})
public class CacheConfiguration {

}
