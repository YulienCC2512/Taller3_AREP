package com.mycompany.ejercicio1;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class Request {

    private final String method;
    private final String rawPath;
    private final String path;
    private final Map<String, List<String>> queryParams = new HashMap<>();

    public Request(String method, String rawPath) {
        this.method = method;
        this.rawPath = (rawPath == null || rawPath.isEmpty()) ? "/" : rawPath;

        int queryIndex = this.rawPath.indexOf('?');
        if (queryIndex >= 0) {
            this.path = this.rawPath.substring(0, queryIndex);
            String query = this.rawPath.substring(queryIndex + 1);
            parseQueryString(query, queryParams);
        } else {
            this.path = this.rawPath;
        }
    }

    private static void parseQueryString(String query, Map<String, List<String>> target) {
        if (query == null || query.isBlank()) {
            return;
        }
        Arrays.stream(query.split("&"))
                .filter(segment -> !segment.isEmpty())
                .forEach(segment -> {
                    int idx = segment.indexOf('=');
                    String key = (idx >= 0) ? segment.substring(0, idx) : segment;
                    String value = (idx >= 0) ? segment.substring(idx + 1) : "";
                    target.computeIfAbsent(decode(key), k -> new ArrayList<>()).add(decode(value));
                });
    }

    private static String decode(String value) {
        return URLDecoder.decode(value.replace("+", " "), StandardCharsets.UTF_8);
    }

    public String getValue(String key) {
        List<String> values = queryParams.get(key);
        return (values == null || values.isEmpty()) ? null : values.get(0);
    }

    public List<String> getValues(String key) {
        List<String> values = queryParams.get(key);
        return (values == null) ? List.of() : List.copyOf(values);
    }
}
