package com.rubal.caching.meta;

import com.rubal.caching.config.CustomCacheRepository;
import com.rubal.caching.indexing.IndexableRepository;
import com.rubal.caching.model.CacheInfo;
import com.palantir.javapoet.AnnotationSpec;
import com.palantir.javapoet.ClassName;
import com.palantir.javapoet.FieldSpec;
import com.palantir.javapoet.JavaFile;
import com.palantir.javapoet.MethodSpec;
import com.palantir.javapoet.ParameterizedTypeName;
import com.palantir.javapoet.TypeSpec;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Modifier;
import javax.tools.JavaFileObject;
import java.io.IOException;
import java.io.Writer;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

public class CacheRepositoryFileWriter {

    public static final String SEARCH_POSTFIX_CACHE_NAME = "SearchIndex";

    private final ProcessingEnvironment processingEnvironment;

    public CacheRepositoryFileWriter(final ProcessingEnvironment processingEnvironment) {
        this.processingEnvironment = processingEnvironment;
    }

    /**
     * Cache Repository Generation Process
     *
     * @param pkg
     * @param ext
     * @param cacheName
     * @param ttl
     * @param unit
     * @param value
     * @param key
     * @param isIndexable
     * @throws IOException
     */
    public void createCacheRepository(String pkg, String ext, String cacheName, long ttl, TimeUnit unit, String value, String key, boolean isIndexable) throws IOException {
        String cls = cacheName + ext;
        String indexableModelCls = StructuredJsonRepositoryFileWriter.CLAZZ_NAME_PREFIX + cacheName;
        // Create the class
        TypeSpec.Builder classBuilder = TypeSpec.classBuilder(cls)
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(AnnotationSpec.builder(Component.class).build())
                .addSuperinterface(CustomCacheRepository.class);
        // TODO: Revisit for in memory cache
        classBuilder.addField(FieldSpec.builder(ParameterizedTypeName.get(ClassName.get(IndexableRepository.class),
                                ClassName.bestGuess(indexableModelCls), ClassName.bestGuess(key), ClassName.bestGuess(value)), "indexableRepository")
                        .addAnnotation(AnnotationSpec.builder(Autowired.class).addMember("required", "$L", isIndexable).build())
                        .addModifiers(Modifier.PRIVATE)
                        .build())
                .addField(FieldSpec.builder(ClassName.get(CacheInfo.class), "cacheInfo")
                        .addModifiers(Modifier.PRIVATE, Modifier.FINAL)
                        .initializer("new CacheInfo($S, $LL, $T.$L)", cacheName, ttl, TimeUnit.class, unit)
                        .build());

        // Cacheable method
        MethodSpec getMethod = getMethod(cacheName, value, key);
        classBuilder.addMethod(getMethod);

        // CachePut method
        MethodSpec putMethod = updateMethod(cacheName, value, key, "put");
        classBuilder.addMethod(putMethod);

        //TODO: Why do we need a put method and an update method?
        // CachePut for update
        MethodSpec updateMethod = updateMethod(cacheName, value, key, "update");
        classBuilder.addMethod(updateMethod);

        // CacheEvict method
        MethodSpec deleteMethod = deleteMethod(cacheName, key);
        classBuilder.addMethod(deleteMethod);

        classBuilder.addMethod(createSearchMethod(indexableModelCls));

        classBuilder.addMethod(MethodSpec.methodBuilder("getCacheInfo")
                .addModifiers(Modifier.PUBLIC)
                .returns(CacheInfo.class)
                .addStatement("return cacheInfo")
                .build());

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

    private static MethodSpec getMethod(String cacheName, String value, String key) {
        return MethodSpec.methodBuilder("get")
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(AnnotationSpec.builder(Cacheable.class)
                        .addMember("value", "$S", cacheName)
                        .addMember("key", "\"#key\"")
                        .addMember("unless", "\"#result == null\"" )
                        .build()
                )
                .returns(ParameterizedTypeName.get(ClassName.get(Optional.class), ClassName.bestGuess(value)))
                .addParameter(ClassName.bestGuess(key), "key")
                .addStatement("return Optional.empty()")
                .build();
    }

    private static MethodSpec updateMethod(String cacheName, String value, String key, String methodName) {
        return MethodSpec.methodBuilder(methodName)
                .addModifiers(Modifier.PUBLIC, Modifier.SYNCHRONIZED)
                .addAnnotation(AnnotationSpec.builder(CachePut.class)
                        .addMember("value", "$S", cacheName)
                        .addMember("key", "\"#key\"")
                        .build())
                .returns(ClassName.bestGuess(value))
                .addParameter(ClassName.bestGuess(key), "key")
                .addParameter(ClassName.bestGuess(value), "value")
                .beginControlFlow("if (indexableRepository.isIndexable())")
                .addStatement("indexableRepository.put(key, value)")
                .endControlFlow()
                .addStatement("return value")
                .build();
    }

    private static MethodSpec deleteMethod(String cacheName, String key) {
        return MethodSpec.methodBuilder("delete")
                .addModifiers(Modifier.PUBLIC, Modifier.SYNCHRONIZED)
                .addAnnotation(AnnotationSpec.builder(CacheEvict.class)
                        .addMember("value", "$S", cacheName)
                        .addMember("key", "\"#key\"")
                        .build())
                .addParameter(ClassName.bestGuess(key), "key")
                .beginControlFlow("if (indexableRepository.isIndexable())")
                .addStatement("indexableRepository.delete(key)")
                .endControlFlow()
                .build();
    }

    private MethodSpec createSearchMethod(String value) {
        return MethodSpec.methodBuilder("search")
                .addModifiers(Modifier.PUBLIC)
                .returns(ParameterizedTypeName.get(ClassName.get(Page.class), ClassName.bestGuess(value)))
                .addParameter(String.class, "query")
                .addParameter(Pageable.class, "pageable")
                .beginControlFlow("if (indexableRepository.isIndexable())")
                .addStatement("return indexableRepository.search(query, pageable)")
                .endControlFlow()
                .addStatement("return new $T($T.of(), pageable, 0)",
                        ParameterizedTypeName.get(ClassName.get(PageImpl.class), ClassName.bestGuess(value)), ClassName.get(List.class))
                .build();
    }

}
