package com.rubal.caching.samplesources;

import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.Builder;
import lombok.Getter;

import java.util.Objects;
import java.util.Optional;

@Getter
@Builder(builderClassName = "RoutingInformationBuilder")
public final class RoutingInformation {

    private final String ruleId;

    private final String organizationId;

    private final String methodURI;

    private final String methodVerb;

    private final String channel;

    private final String applicationIdentity;

    public Optional<String> getChannel() {
        return Optional.ofNullable(channel);
    }

    public Optional<String> getApplicationIdentity() {
        return Optional.ofNullable(applicationIdentity);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RoutingInformation that = (RoutingInformation) o;
        return ruleId.equals(that.ruleId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ruleId);
    }

    @JsonPOJOBuilder(withPrefix = "")
    public static class RoutingInformationBuilder {

    }
}