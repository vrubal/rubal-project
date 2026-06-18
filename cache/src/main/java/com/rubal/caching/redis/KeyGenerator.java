package com.rubal.caching.redis;

import com.github.f4b6a3.ulid.UlidCreator;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.data.keyvalue.core.IdentifierGenerator;
import org.springframework.data.util.TypeInformation;
import org.springframework.util.ClassUtils;

public enum KeyGenerator {//implements IdentifierGenerator {
    INSTANCE;

    private KeyGenerator(){}

    @SuppressWarnings("unchecked")
    //@Override
    public <T> T generateIdentifierOfType(TypeInformation<T> identifierType) {
        Class<?> type = identifierType.getType();
        if (ClassUtils.isAssignable(String.class, type)) {
            return (T) UlidCreator.getMonotonicUlid().toString();
        } else {
            throw new InvalidDataAccessApiUsageException(String.format("Identifier cannot be generated for %s; Supported types is: String", identifierType.getType().getName()));
        }
    }

}
