package com.fiserv.bsol.esf.caching.annotations.sources;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import org.junit.jupiter.api.MethodOrderer;

@Getter
@ToString
@Builder()
@JsonDeserialize(builder = com.rubal.caching.samplesources.RoutingInquiry.RoutingInquiryBuilder.class)
public class RoutingInquiry {

    private String httpMethodURI;

    private MethodOrderer.MethodName httpVerb;

    private String organizationId;

    private String providerName;

    @JsonPOJOBuilder(withPrefix = "")
    public static class RoutingInquiryBuilder {
    }

}