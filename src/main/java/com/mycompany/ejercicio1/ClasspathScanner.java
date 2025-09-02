package com.mycompany.ejercicio1;

import java.io.File;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

public class ClasspathScanner {

    public List<Class<?>> findRestControllers() {
        try {
            // obtenemos todos los roots y los convertimos en clases escaneadas
            List<Class<?>> classes = Collections.list(
                            Thread.currentThread().getContextClassLoader().getResources("")
                    ).stream()
                    .filter(url -> "file".equals(url.getProtocol()))
                    .map(this::scanFromUrl)
                    .flatMap(List::stream)
                    .toList();

            // filtramos solo los anotados con @RestController
            return classes.stream()
                    .filter(c -> c.isAnnotationPresent(RestController.class))
                    .collect(Collectors.toList());
        } catch (Exception ex) {
            throw new RuntimeException("Error durante el escaneo de clases", ex);
        }
    }

    private List<Class<?>> scanFromUrl(URL url) {
        try {
            File root = new File(url.toURI());
            return scanDirectory(root, root);
        } catch (Exception e) {
            return Collections.emptyList();
        }
    }

    private List<Class<?>> scanDirectory(File root, File dir) {
        File[] files = dir.listFiles();
        if (files == null) return Collections.emptyList();

        List<Class<?>> found = new ArrayList<>();
        for (File file : files) {
            if (file.isDirectory()) {
                found.addAll(scanDirectory(root, file));
            } else if (file.getName().endsWith(".class")) {
                toClassName(root, file).ifPresent(name -> {
                    try {
                        found.add(Class.forName(name));
                    } catch (Throwable ignored) {
                    }
                });
            }
        }
        return found;
    }

    private Optional<String> toClassName(File root, File cls) {
        String rootPath = root.getAbsolutePath();
        String clsPath = cls.getAbsolutePath();
        if (!clsPath.startsWith(rootPath)) return Optional.empty();

        String relative = clsPath.substring(rootPath.length() + 1)
                .replace(File.separatorChar, '.');

        return Optional.of(relative.substring(0, relative.length() - ".class".length()));
    }
}
