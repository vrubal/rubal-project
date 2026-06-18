package com.rubal.caching.samplesources;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Getter
@ToString
@Builder(builderClassName = "RoutingRulesBuilder")
@JsonDeserialize(builder = RoutingRules.RoutingRulesBuilder.class)
public class RoutingRules {

    private List<RoutingInformation> rules;

    @JsonPOJOBuilder(withPrefix = "")
    public static class RoutingRulesBuilder {
    }

}