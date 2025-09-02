package com.mycompany.ejercicio1;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

// anotación para mapear métodos GET
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface GetMapping {

    public String value();
}
