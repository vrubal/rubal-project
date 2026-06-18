package com.rubal.caching.validation.checks;

import com.rubal.caching.validation.SoftCheck;

import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic;

public class EqualsAndHashCodeCheck implements SoftCheck<TypeMirror> {

    @Override
    public void check(ProcessingEnvironment processingEnvironment, TypeMirror typeMirror) {
        boolean hasEquals = false;
        boolean hasHashCode = false;

        Elements elementUtils = processingEnvironment.getElementUtils();
        Types typeUtils = processingEnvironment.getTypeUtils();
        TypeElement typeElement = (TypeElement) typeUtils.asElement(typeMirror);

        for (Element element : elementUtils.getAllMembers(typeElement)) {
            if (element.getKind() == ElementKind.METHOD) {
                ExecutableElement method = (ExecutableElement) element;
                if (method.getSimpleName().toString().equals("equals") &&
                        method.getParameters().size() == 1 &&
                        method.getParameters().get(0).asType().toString().equals(Object.class.getName()) &&
                        !method.getEnclosingElement().asType().toString().equals(Object.class.getName())) {
                    hasEquals = true;
                }
                if (method.getSimpleName().toString().equals("hashCode") &&
                        method.getParameters().isEmpty() &&
                        !method.getEnclosingElement().asType().toString().equals(Object.class.getName())) {
                    hasHashCode = true;
                }
            }
        }

        if (!hasEquals || !hasHashCode) {
            Messager messager = processingEnvironment.getMessager();
            messager.printMessage(Diagnostic.Kind.WARNING, "Class " + typeMirror.toString() + " must implement equals and hashCode methods.");
            //throw new IllegalStateException("Class " + typeMirror.toString() + " must implement equals and hashCode methods.");
        }
    }
}