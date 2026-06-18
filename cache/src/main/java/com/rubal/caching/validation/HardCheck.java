package com.rubal.caching.validation;

import javax.annotation.processing.ProcessingEnvironment;

public interface HardCheck<T> extends Check<T> {
    @Override
    void check(ProcessingEnvironment processingEnvironment, T typeMirror) throws IllegalStateException;
}