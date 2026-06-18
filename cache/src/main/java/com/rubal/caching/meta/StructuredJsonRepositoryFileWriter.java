package com.rubal.caching.meta;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.rubal.caching.annotations.CacheDefinition;
import com.rubal.caching.annotations.IndexCreationMode;
import com.rubal.caching.annotations.IndexMapping;
import com.rubal.caching.indexing.IndexableRepository;
import com.rubal.caching.model.IndexableCacheInfo;
import com.rubal.caching.model.IndexableData;
import com.rubal.caching.model.IndexedFieldsInfo;
import com.rubal.caching.redis.ESFSerializer;
import com.github.f4b6a3.ulid.UlidCreator;
import com.palantir.javapoet.AnnotationSpec;
import com.palantir.javapoet.ClassName;
import com.palantir.javapoet.FieldSpec;
import com.palantir.javapoet.JavaFile;
import com.palantir.javapoet.MethodSpec;
import com.palantir.javapoet.ParameterizedTypeName;
import com.palantir.javapoet.TypeSpec;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import redis.clients.jedis.RedisClient;
import redis.clients.jedis.exceptions.JedisDataException;
import redis.clients.jedis.search.Document;
import redis.clients.jedis.search.FTSearchParams;
import redis.clients.jedis.search.SearchResult;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Modifier;
import javax.tools.JavaFileObject;
import java.io.IOException;
import java.io.Writer;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class StructuredJsonRepositoryFileWriter {

    public static final String SEARCH_POSTFIX_CACHE_NAME = "SearchIndex";
    public static final String CLAZZ_NAME_SUFFIX = "Repository";
    public static final String CLAZZ_NAME_PREFIX = "Indexable";

    private final ProcessingEnvironment processingEnvironment;

    public StructuredJsonRepositoryFileWriter(final ProcessingEnvironment processingEnvironment) {
        this.processingEnvironment = processingEnvironment;
    }

    /**
     * Cache Repository Generation Process
     *
     * @param pkg
     * @param cacheName
     * @param ttl
     * @param unit
     * @param value
     * @param key
     * @param indexable
     * @throws IOException
     */
    public void createJsonRepository(String pkg, String cacheName, String key, String value,
                                     CacheDefinition cacheDefinition) throws IOException {
        String cls = CLAZZ_NAME_PREFIX + cacheName + CLAZZ_NAME_SUFFIX;
        String indexableModelCls = CLAZZ_NAME_PREFIX + cacheName;

        // Create the class
        TypeSpec.Builder classBuilder = TypeSpec.classBuilder(cls)
                .addModifiers(Modifier.PUBLIC)
                .addSuperinterface(ParameterizedTypeName.get(ClassName.get(IndexableRepository.class),
                        ClassName.bestGuess(indexableModelCls), ClassName.bestGuess(key), ClassName.bestGuess(value)))
                .addAnnotation(AnnotationSpec.builder(Component.class).build())
                .addAnnotation(AnnotationSpec.builder(ConditionalOnProperty.class)
                        .addMember("value", "$S", "spring.cache.type")
                        .addMember("havingValue", "$S", "redis")
                        .build());

        classBuilder.addField(FieldSpec.builder(ClassName.get(RedisClient.class), "redisClient")
                        .addAnnotation(Autowired.class)
                        .addModifiers(Modifier.PRIVATE)
                        .addAnnotation(AnnotationSpec.builder(Qualifier.class)
                                .addMember("value", "$S", "RedisClientSecondary")
                                .build())
                        .build())
                .addField(FieldSpec.builder(ClassName.get(ESFSerializer.class), "serializer")
                        .addAnnotation(Autowired.class)
                        .addModifiers(Modifier.PRIVATE)
                        .build())
                .addField(FieldSpec.builder(ClassName.get(Logger.class), "logger")
                        .addModifiers(Modifier.PRIVATE, Modifier.STATIC, Modifier.FINAL)
                        .initializer("$T.getLogger($T.class)", ClassName.get(LoggerFactory.class), ClassName.bestGuess(cls))
                        .build());

        // TODO: Add field for IndexedFieldsInfo

        // CachePut method
        MethodSpec putMethod = updateMethod(cacheName, value, key, "put");
        classBuilder.addMethod(putMethod);

        // CacheDelete method
        MethodSpec deleteMethod = deleteMethod(cacheName, key);
        classBuilder.addMethod(deleteMethod);

        // indexSearch method
        String fullIndexableModelCls = pkg + "." + indexableModelCls;
        String indexName = indexableModelCls + cacheDefinition.mapping().suffix();
        String indexAlias = indexName;

        if (!cacheDefinition.mapping().indexAlias().isEmpty()) {
            indexAlias = cacheDefinition.mapping().indexAlias();
        }
        classBuilder.addMethod(createSearchMethod(cacheName, fullIndexableModelCls, indexAlias));
        classBuilder.addMethod(createMapToMethod(CLAZZ_NAME_PREFIX + cacheName, indexableModelCls));

        // Add the methods to fulfill the IndexableRepository interface
        classBuilder.addMethod(MethodSpec.methodBuilder("isIndexable")
                .addModifiers(Modifier.PUBLIC)
                .returns(boolean.class)
                .addStatement("return $L", cacheDefinition.indexable())
                .build());


        classBuilder.addMethod(getIndexableCacheInfoMethod(cacheName, indexName, cacheDefinition.mapping()));
        // End methods for interface

        // Build the class
        TypeSpec cacheRepositoryClass = classBuilder.build();

        // Create the Java file
        JavaFile javaFile = JavaFile.builder(pkg, cacheRepositoryClass).build();

        // Write the file
        JavaFileObject javaFileObject = processingEnvironment.getFiler().createSourceFile(pkg + "." + cls);
        try (Writer writer = javaFileObject.openWriter()) {
            javaFile.writeTo(writer);
        }
    }

    public void createIndexableModel(String pkg, String cacheName, String key, String value) throws IOException {
        String cls = CLAZZ_NAME_PREFIX + cacheName;

        // Create the class
        TypeSpec.Builder classBuilder = TypeSpec.classBuilder(cls)
                .addModifiers(Modifier.PUBLIC)
                .superclass(ParameterizedTypeName.get(
                        ClassName.get(IndexableData.class), ClassName.bestGuess(key), ClassName.bestGuess(value)));

        // Build the class
        TypeSpec indexableModelClass = classBuilder.build();

        // Create the Java file
        JavaFile javaFile = JavaFile.builder(pkg, indexableModelClass).build();

        // Write the file
        JavaFileObject javaFileObject = processingEnvironment.getFiler().createSourceFile(pkg + "." + cls);
        try (Writer writer = javaFileObject.openWriter()) {
            javaFile.writeTo(writer);
        }
    }

    public MethodSpec getIndexableCacheInfoMethod(String cacheName, String indexName, IndexMapping mapping) {
        StringBuilder indexablePropertiesAliasString = new StringBuilder("List.of(");

        if (mapping.fields().length > 0) {
            indexablePropertiesAliasString.append(Arrays.stream(mapping.fields())
                    .map(field -> "new IndexedFieldsInfo(\"" + field.path() + "\", " + "\"" + field.alias() + "\", " + field.sortable() + ", " + field.weight() + ")")
                    .collect(Collectors.joining(",\n")));
        }
        indexablePropertiesAliasString.append(")");

        String keyPrefix = cacheName + ":Idx:";
        return MethodSpec.methodBuilder("getIndexableCacheInfo")
                .addModifiers(Modifier.PUBLIC)
                .returns(IndexableCacheInfo.class)
                .addStatement("$T fieldsInfo = $L",
                        ParameterizedTypeName.get(ClassName.get(List.class), ClassName.get(IndexedFieldsInfo.class)), indexablePropertiesAliasString.toString())
                .addStatement("return new $T($S, $S, $S, $T.$L, fieldsInfo)", IndexableCacheInfo.class, indexName, mapping.indexAlias(), keyPrefix, IndexCreationMode.class, mapping.mode())
                .build();
    }

    public MethodSpec deleteMethod(String cacheName, String key) {
        return MethodSpec.methodBuilder("delete")
                .addModifiers(Modifier.PUBLIC, Modifier.SYNCHRONIZED)
                .addParameter(ClassName.bestGuess(key), "key")
                .beginControlFlow("try")
                .addStatement("String cacheKey = serializer.serializeAsString(key)")
                .addStatement("String indexIdKey = getIndexableCacheInfo().keyPrefix() + cacheKey")
                .addStatement("$T<String> documentId  = Optional.ofNullable(redisClient.get(indexIdKey))", Optional.class)
                .beginControlFlow("documentId.ifPresent(id -> ")
                .beginControlFlow("try( $T transaction = redisClient.transaction(false) ) ", ClassName.get("redis.clients.jedis", "AbstractTransaction"))
                .addStatement("transaction.watch(indexIdKey)")
                .addStatement("transaction.multi()")
                .addStatement("transaction.expire(id, 0)")
                .addStatement("transaction.expire(indexIdKey, 0)")
                .addStatement("transaction.exec()")
                .endControlFlow()
                .endControlFlow(")")
                .nextControlFlow("catch ($T e)", JsonProcessingException.class)
                .addStatement("logger.error($S + e.getMessage())", "Error while removing entry from index for " + cacheName + ": ")
                .endControlFlow()
                .build();
    }

    private MethodSpec updateMethod(String cacheName, String value, String key, String methodName) {
        String cls = CLAZZ_NAME_PREFIX + cacheName;

        return MethodSpec.methodBuilder(methodName)
                .addModifiers(Modifier.PUBLIC, Modifier.SYNCHRONIZED)
                .returns(ClassName.bestGuess(cls))
                .addParameter(ClassName.bestGuess(key), "key")
                .addParameter(ClassName.bestGuess(value), "value")
                .addStatement("$L indexed = new $L()", cls, cls)
                .beginControlFlow("try")
                .addStatement("String cacheKey = serializer.serializeAsString(key)")
                .addStatement("String indexIdKey = getIndexableCacheInfo().keyPrefix() + cacheKey")
                .addStatement("String existingDocumentId = redisClient.get(indexIdKey)")
                .beginControlFlow("if (existingDocumentId == null)")
                .addStatement("indexed.setId($T.getMonotonicUlid().toString())", UlidCreator.class)
                .addStatement("indexed.setKey(key)")
                .addStatement("indexed.setValue(value)")
                .addStatement("String documentId = getIndexableCacheInfo().keyPrefix() + indexed.getId()")
                .addStatement("String jsonValue = serializer.serializeAsString(indexed)")
                .addStatement("Long setResult = redisClient.setnx(indexIdKey, documentId)")
                .beginControlFlow("if (setResult == 1L)")
                .addStatement("redisClient.jsonSet(documentId, jsonValue)")
                .endControlFlow()
                .endControlFlow()
                .nextControlFlow("catch ($T | $T e)", JsonProcessingException.class, JedisDataException.class)
                .addStatement("logger.error($S + e.getMessage())", "Error while adding entry in index for " + cacheName + ": ")
                .endControlFlow()
                .addStatement("return indexed")
                .build();
    }

    private MethodSpec createSearchMethod(String cacheName, String value, String alias) {
        return MethodSpec.methodBuilder("search")
                .addModifiers(Modifier.PUBLIC)
                .returns(ParameterizedTypeName.get(ClassName.get(Page.class), ClassName.bestGuess(value)))
                .addParameter(String.class, "query")
                .addParameter(Pageable.class, "pageable")
                // TODO: remove control flow and pass down alias explicitly
                .addStatement("String alias = getIndexableCacheInfo().indexAlias()")
                .beginControlFlow("if (alias.isEmpty())")
                .addStatement("alias = getIndexableCacheInfo().name()")
                .endControlFlow()
                .addStatement("$T ftSearchParams = $T.searchParams().limit((int) pageable.getOffset(), pageable.getPageSize())",
                        ClassName.get(FTSearchParams.class), ClassName.get(FTSearchParams.class))
                .addStatement("$T results = redisClient.ftSearch(alias, query, ftSearchParams)", ClassName.get(SearchResult.class))
                .addStatement("$T entries = results.getDocuments().stream().map(result -> mapTo$L(result)).collect($T.toList())",
                        ParameterizedTypeName.get(ClassName.get(List.class), ClassName.bestGuess(value)), CLAZZ_NAME_PREFIX + cacheName,
                        ClassName.get(Collectors.class))
                .addStatement("return new $T(entries, pageable, results.getTotalResults())",
                        ParameterizedTypeName.get(ClassName.get(PageImpl.class), ClassName.bestGuess(value)))
                .build();
    }

    private MethodSpec createMapToMethod(String name, String returnType) {
        return MethodSpec.methodBuilder("mapTo" + name)
                .addModifiers(Modifier.PRIVATE)
                .returns(ClassName.bestGuess(returnType))
                .addParameter(ClassName.get(Document.class), "result")
                .addStatement("$T value = result.get($S)", ClassName.get(Object.class), "$")
                .beginControlFlow("try")
                .addStatement("return serializer.deserialize((String) value, $T.class)",
                        ClassName.bestGuess(returnType))
                .nextControlFlow("catch ($T e)", ClassName.get(JsonProcessingException.class))
                .addStatement("logger.debug($S + e.getMessage())", "Error in " + ".mapTo" + name + ": ")
                .addStatement("return new $T()", ClassName.bestGuess(returnType))
                .endControlFlow()
                .build();
    }

}
