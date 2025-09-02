package com.mycompany.ejercicio1;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.*;

/**
 * Router muy simple que asocia rutas HTTP con métodos anotados
 * en controladores marcados con @RestController.
 */
public class WebRouter {

    /**
     * Representa una ruta HTTP vinculada a un método concreto de un bean.
     */
    public static class Route {
        final Object bean;
        final Method method;
        final String produces;

        Route(Object bean, Method method, String produces) {
            this.bean = bean;
            this.method = method;
            this.produces = produces;
        }
    }

    private final Map<String, Route> routes = new HashMap<>();

    /**
     * Registra todas las rutas de un bean que tenga métodos con @GetMapping.
     */
    public void register(Object bean) {
        for (var m : bean.getClass().getMethods()) {
            var mapping = m.getAnnotation(GetMapping.class);
            if (mapping == null) continue;

            if (m.getReturnType() != String.class) {
                continue;
            }

            var path = mapping.value();
            routes.put(path, new Route(bean, m, mapping.value()));

        }
    }

    /**
     * Busca una ruta por su path.
     */
    public Route findRoute(String path) {
        return routes.get(path);
    }

    public Object invoke(Route route, Request req) throws Exception {
        var params = route.method.getParameters();
        var args = new Object[params.length];

        for (int i = 0; i < params.length; i++) {
            var type = params[i].getType();

            if (type == Request.class) {
                args[i] = req;
                continue;
            }

            var rp = getAnnotation(params[i], RequestParam.class);
            if (rp != null) {
                var val = Optional.ofNullable(req.getValue(rp.value()))
                        .filter(v -> !v.isEmpty())
                        .orElse(rp.defaultValue());
                args[i] = val;
            } else {
                args[i] = null;
            }
        }
        return route.method.invoke(route.bean, args);
    }

    /**
     * Obtiene una anotación concreta de un parámetro, si existe.
     */
    private static <A extends Annotation> A getAnnotation(Parameter p, Class<A> ann) {
        for (var a : p.getAnnotations()) {
            if (ann.isInstance(a)) return ann.cast(a);
        }
        return null;
    }

}
