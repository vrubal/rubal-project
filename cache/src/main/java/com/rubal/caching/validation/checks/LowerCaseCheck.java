package com.rubal.caching.validation.checks;

import com.rubal.caching.validation.SoftCheck;

import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.tools.Diagnostic;

public class LowerCaseCheck implements SoftCheck<String> {

    @Override
    public void check(ProcessingEnvironment processingEnvironment, String type) {
        if (!type.equals(type.toLowerCase())) {
            Messager messager = processingEnvironment.getMessager();
            messager.printMessage(Diagnostic.Kind.WARNING, "Invalid " + type + " is not in lowercase.");
        }
    }
}