package com.rubal.caching.redis;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.convert.converter.Converter;

/**
 * convertor for storing objects as json string
 */
public class CacheConverter implements Converter<Object, String> {
    /**
     * object mapper
     */
    private final ObjectMapper om;

    /**
     * constructor taking in object mapper
     *
     * @param objectMapper the object mapper to be used
     */
    public CacheConverter(ObjectMapper objectMapper) {
        this.om = objectMapper;
    }

    /**
     * convert object to string
     *
     * @param o object to be converted
     * @return string represented by object
     */
    @Override
    public String convert(Object o) {
        try {
            return om.writeValueAsString(o);
        } catch (JsonProcessingException jpe) {
            throw new UnsupportedOperationException(
                    "json write error " + jpe.getMessage());
        }
    }
}
