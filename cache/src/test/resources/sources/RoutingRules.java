package com.fiserv.bsol.esf.caching.annotations.sources;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import com.rubal.caching.samplesources.RoutingInformation;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Getter
@ToString
@Builder(builderClassName = "RoutingRulesBuilder")
@JsonDeserialize(builder = com.rubal.caching.samplesources.RoutingRules.RoutingRulesBuilder.class)
public class RoutingRules {

    private List<RoutingInformation> rules;

    @JsonPOJOBuilder(withPrefix = "")
    public static class RoutingRulesBuilder {
    }

}