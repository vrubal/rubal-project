package com.rubal.caching.processor;

import com.google.testing.compile.Compilation;
import com.google.testing.compile.JavaFileObjects;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.annotation.processing.Processor;
import java.lang.reflect.InvocationTargetException;

import static com.google.testing.compile.CompilationSubject.assertThat;
import static com.google.testing.compile.Compiler.javac;

public class CacheEnableProcessorTest {

    private Class<?> lombokAnnotationProcessor;
    private Class<?> lombokClaimingProcessor;

    @BeforeEach
    public void setup() throws ClassNotFoundException {
        lombokAnnotationProcessor = getClass().getClassLoader().loadClass("lombok.launch.AnnotationProcessorHider$AnnotationProcessor");
        lombokClaimingProcessor = getClass().getClassLoader().loadClass("lombok.launch.AnnotationProcessorHider$ClaimingProcessor");
    }

    @Test
    void testProcess() throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        var compilation = javac()
                .withProcessors(
                        (Processor) lombokAnnotationProcessor.getDeclaredConstructor().newInstance(),
                        (Processor) lombokClaimingProcessor.getDeclaredConstructor().newInstance(),
                        new CacheEnableProcessor())
                .compile(JavaFileObjects.forResource("sources/CacheConfiguration.java"));
        // We want to assert succeededWithoutWarnings() but we need to make sure everyone is on java 17.
        assertThat(compilation).succeeded();
    }

}
