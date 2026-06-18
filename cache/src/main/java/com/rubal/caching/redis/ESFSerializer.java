package com.rubal.caching.redis;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rubal.caching.model.ESFWrapper;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;

import java.lang.reflect.Modifier;

/**
 * Serializer for serialize/deserialize functions
 */
public class ESFSerializer implements RedisSerializer<Object> {

    /**
     * object mapper
     */
    private ObjectMapper om;

    /**
     * constructor taking in object mapper
     *
     * @param om
     */
    public ESFSerializer(ObjectMapper om) {
        this.om = om;
    }

    /**
     * serialize the object to bytes
     *
     * @param source the object
     * @return the serialized bytes
     * @throws SerializationException if json is invalid
     */
    public byte[] serialize(Object source) throws SerializationException {
        if (source == null) return null;
        try {
            var tempValue = source;
            if (Modifier.isFinal(source.getClass().getModifiers())) {
                tempValue =  new ESFWrapper.ESFWrapperBuilder()
                        .value(source)
                        .build();
            }
            return om.writeValueAsBytes(tempValue);
        } catch (JsonProcessingException e) {
            throw new SerializationException("serialize error " + e.getMessage(), e);
        }
    }

    /**
     * serialized from bytes to object
     *
     * @param source the bytes
     * @return the object
     * @throws SerializationException if json is invalid
     */
    public Object deserialize(byte[] source) throws SerializationException {
        if (source == null || source.length == 0) return null;

        try {
            Object value = om.readValue(source, Object.class);
            if (value instanceof ESFWrapper wrapper) {
                return wrapper.getValue();
            }
            return value;
        } catch (Exception ex) {
            throw new SerializationException("deserialize error " + ex.getMessage(), ex);
        }
    }

    public String serializeAsString(Object o) throws JsonProcessingException {
        return om.writeValueAsString(o);
    }

    public <T> T deserialize(Object object, Class<T> valueType) throws JsonProcessingException {
        return om.readValue((String) object, valueType);
    }

}
