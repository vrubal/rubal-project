package com.rubal.transformers.jdk;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author vrubal
 * @created
 * @project rubal-project
 */
@AllArgsConstructor
@Getter
@Setter
@ToString
public class Person {
    private String name;
    private int height;
    public String city;
}
