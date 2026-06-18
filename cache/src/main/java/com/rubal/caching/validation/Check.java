package com.rubal.caching.validation;

import javax.annotation.processing.ProcessingEnvironment;

public interface Check<T> {
    void check(ProcessingEnvironment processingEnvironment, T type);
}