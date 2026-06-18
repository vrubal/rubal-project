package com.rubal.caching.samplesources;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import org.junit.jupiter.api.MethodOrderer;

import java.util.Objects;

@Getter
@ToString
@Builder()
@JsonDeserialize(builder = RoutingInquiry.RoutingInquiryBuilder.class)
public class RoutingInquiry {

    private String httpMethodURI;

    private MethodOrderer.MethodName httpVerb;

    private String organizationId;

    private String providerName;

    @JsonPOJOBuilder(withPrefix = "")
    public static class RoutingInquiryBuilder {
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        RoutingInquiry that = (RoutingInquiry) o;
        return Objects.equals(httpMethodURI, that.httpMethodURI) && Objects.equals(httpVerb, that.httpVerb) && Objects.equals(organizationId, that.organizationId) && Objects.equals(providerName, that.providerName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(httpMethodURI, httpVerb, organizationId, providerName);
    }
}