package com.mycompany.ejercicio1;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
public @interface RequestParam {


    String value();

    String defaultValue() default "";

    /**
     * Indica si el parámetro es obligatorio.
     */
    boolean required() default false;
}
