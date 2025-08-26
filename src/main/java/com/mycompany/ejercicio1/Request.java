package main.java.com.mycompany.ejercicio1;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Representa una petición HTTP (simplificada).
 * Julian Santiago Cardenas
 */
public class Request {

    private String method;
    private String path;
    private Map<String, String> headers;
    private String body;

    public Request(String method, String path, Map<String, String> headers, String body) {
        this.method = method;
        this.path = path;
        this.headers = headers;
        this.body = body;
    }

    public String getMethod() {
        return method;
    }

    public String getPath() {
        return path;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public String getBody() {
        return body;
    }


    public static Request build(BufferedReader in) throws IOException {

        String inputLine = in.readLine();
        if (inputLine == null || inputLine.isEmpty()) {
            // Si no hay request, devolvemos un request por defecto a "/"
            return new Request("GET", "/", new HashMap<>(), "");
        }

        String[] requestParts = inputLine.split(" ");
        String method = requestParts[0];
        String path = requestParts.length > 1 ? requestParts[1] : "/";


        Map<String, String> headers = new HashMap<>();
        String line;
        while ((line = in.readLine()) != null && !line.isEmpty()) {
            String[] headerParts = line.split(":", 2);
            if (headerParts.length == 2) {
                headers.put(headerParts[0].trim(), headerParts[1].trim());
            }
        }

        String body = "";
        if (headers.containsKey("Content-Length")) {
            int length = Integer.parseInt(headers.get("Content-Length"));
            char[] buffer = new char[length];
            in.read(buffer, 0, length);
            body = new String(buffer);
        }

        return new Request(method, path, headers, body);
    }
}
