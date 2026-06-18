package com.rubal.caching.processor;

import com.rubal.caching.annotations.CacheDefinition;
import com.rubal.caching.annotations.CacheEnable;
import com.rubal.caching.meta.CacheRepositoryFileWriter;
import com.rubal.caching.meta.StructuredJsonRepositoryFileWriter;
import com.rubal.caching.validation.checks.EqualsAndHashCodeCheck;
import com.rubal.caching.validation.checks.LowerCaseCheck;
import com.google.auto.service.AutoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.MirroredTypeException;
import javax.lang.model.type.TypeMirror;
import javax.tools.Diagnostic;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * CacheEnable Annotation Processor<br/>
 * processor uses the @CacheEnable annotation<br/>
 * and generates code to support the caching
 */
@SupportedAnnotationTypes("com.fiserv.bsol.esf.caching.annotations.CacheEnable")
@SupportedSourceVersion(SourceVersion.RELEASE_17)
@AutoService(Processor.class)
public class CacheEnableProcessor extends AbstractProcessor {

    private static final Logger logger = LoggerFactory.getLogger(CacheEnableProcessor.class);

    /**
     * The list of caches
     */
    private final List<String> caches = new ArrayList<>();

    /**
     * Utility to create a cache repository based of configuration provided through annotation
     */
    private CacheRepositoryFileWriter cacheRepositoryFileWriter;
    private StructuredJsonRepositoryFileWriter jsonRepositoryFileWriter;

    /**
     * Checks for equal and hashcode implementation for key cache definitions.
     */
    private EqualsAndHashCodeCheck equalsAndHashCodeCheck;
    /**
     * Checks for lowercase name definitions on cache name
     */
    private LowerCaseCheck lowerCaseCheck;

    public CacheEnableProcessor() {
        super();
        this.cacheRepositoryFileWriter = new CacheRepositoryFileWriter(processingEnv);
        this.jsonRepositoryFileWriter = new StructuredJsonRepositoryFileWriter(processingEnv);
        this.equalsAndHashCodeCheck = new EqualsAndHashCodeCheck();
        this.lowerCaseCheck = new LowerCaseCheck();
    }

    /**
     * process the set of annotation and generate cache definitions
     *
     * @param annotations specifically CacheEnable
     * @param roundEnv    provided environment
     * @return true after processing done
     */
    @Override
    public boolean process(Set<? extends TypeElement> annotations,
                           RoundEnvironment roundEnv) {
        Element thisElem = null;
        try {
            for (TypeElement annotation : annotations) {
                Set<? extends Element> elems = roundEnv.getElementsAnnotatedWith(annotation);
                // for each element generate
                for (Element elem : elems) {
                    thisElem = elem;
                    caches.addAll(generateSource(thisElem));
                }
            }
        } catch (IOException iox) {
            // annotation error
            processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR,
                    "Annotation Error - " + iox.getMessage(), thisElem);
        }
        return true;
    }

    /**
     * generate source code for each cache definition
     *
     * @param elem the CacheEnable element that has the cache definition
     * @return list of cache repository names
     * @throws IOException
     */
    private List<String> generateSource(Element elem) throws IOException {
        CacheEnable cea = elem.getAnnotation(CacheEnable.class);
        String pkg = "com.fiserv.banksolutions.esf.client.caching";
        CacheDefinition[] cd = cea.caches();
        String ext = "CacheRepository";
        List<String> ret = new ArrayList<>();
        // for each cache definition, generate code
        Arrays.stream(cd)
                .peek(cache -> {
                    try {
                        generateSource(pkg, ext, cache);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                })
                .map(CacheDefinition::name)
                .forEach(ret::add);

        return ret;
    }

    /**
     * generate source code at package level for each cache definition
     *
     * @param pkg
     * @param ext
     * @param cacheDefinition
     * @throws IOException
     */
    private void generateSource(String pkg, String ext, CacheDefinition cacheDefinition) throws IOException {
        String cacheName = cacheDefinition.name();
        String key;
        String value;
        long ttl;
        TimeUnit unit;

        // get the cache key and value first
        TypeMirror keyTypeMirror;
        TypeMirror valueTypeMirror;
        try {
            keyTypeMirror = processingEnv.getElementUtils().getTypeElement(cacheDefinition.key().getName()).asType();
        } catch (MirroredTypeException mteKey) {
            keyTypeMirror = mteKey.getTypeMirror();
        }
        try {
            valueTypeMirror = processingEnv.getElementUtils().getTypeElement(cacheDefinition.value().getName()).asType();
        } catch (MirroredTypeException mteVal) {
            valueTypeMirror = mteVal.getTypeMirror();
        }

        key = keyTypeMirror.toString();
        value = valueTypeMirror.toString();

        ttl = cacheDefinition.ttl();
        unit = cacheDefinition.unit();

        equalsAndHashCodeCheck.check(processingEnv, keyTypeMirror);
        // TODO: Below is just an example of a soft check, remove if don't need it.
        lowerCaseCheck.check(processingEnv, cacheDefinition.name());

        jsonRepositoryFileWriter = new StructuredJsonRepositoryFileWriter(processingEnv);
        jsonRepositoryFileWriter.createIndexableModel(pkg, cacheName, key, value);
        jsonRepositoryFileWriter.createJsonRepository(pkg, cacheName, key, value, cacheDefinition);
        cacheRepositoryFileWriter = new CacheRepositoryFileWriter(processingEnv);
        cacheRepositoryFileWriter.createCacheRepository(pkg, ext, cacheName, ttl, unit, value, key, cacheDefinition.indexable());
    }
}
