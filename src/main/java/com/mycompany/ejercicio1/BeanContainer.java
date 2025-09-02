package com.mycompany.ejercicio1;

import java.util.HashMap;
import java.util.Map;

public class BeanContainer {

    private final Map<Class<?>, Object> instances = new HashMap<>();

    public <T> T getOrCreate(Class<T> clazz) {
        try {
            Object obj = instances.computeIfAbsent(clazz, type -> {
                if (!type.isAnnotationPresent(RestController.class)) {
                    throw new IllegalArgumentException(
                            "@RestController faltante en la clase: " + type.getName()
                    );
                }
                try {
                    return type.getDeclaredConstructor().newInstance();
                } catch (Exception ex) {
                    throw new RuntimeException("No se pudo crear instancia de " + type.getName(), ex);
                }
            });

            return clazz.cast(obj);
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Error al procesar " + clazz.getName(), e);
        }
    }
}
