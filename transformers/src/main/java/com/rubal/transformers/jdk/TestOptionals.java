package com.rubal.transformers.jdk;

import javax.swing.text.html.Option;
import java.util.Optional;



public class TestOptionals {

    public static void main(String[] args) {
        Optional<String> stringOptional = Optional.empty();
        System.out.println(stringOptional.isPresent());
        stringOptional = Optional.ofNullable(null);
        System.out.println(stringOptional.isPresent());
        stringOptional = Optional.of("Test");
        System.out.println(stringOptional.isPresent());



    }
}
