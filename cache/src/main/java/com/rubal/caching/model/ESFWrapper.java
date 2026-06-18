package com.rubal.caching.model;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@Builder(builderClassName = "ESFWrapperBuilder")
@EqualsAndHashCode
@JsonDeserialize(builder = ESFWrapper.ESFWrapperBuilder.class)
public class ESFWrapper {

    @JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY, property = "@class")
    private Object value;

    @JsonPOJOBuilder(withPrefix = "")
    public static class ESFWrapperBuilder {
        private Object value;

        public ESFWrapperBuilder() {
            // Jackson not auto-generating constructor
        }

        public ESFWrapperBuilder value(final Object value) {
            this.value = value;
            return this;
        }
    }

}
