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
@Setter
@Getter
@ToString
public class Employee {
    private int height;
    private String name;
    private double city;
}
