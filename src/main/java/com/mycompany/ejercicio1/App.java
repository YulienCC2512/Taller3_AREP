package main.java.com.mycompany.ejercicio1;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;

public class App {

    private static final Map<String, BiConsumer<Request, Response>> routes = new HashMap<>();

    public static void get(String path, BiConsumer<Request, Response> handler) {
        routes.put("GET:" + path, handler);
    }

    public static void post(String path, BiConsumer<Request, Response> handler) {
        routes.put("POST:" + path, handler);
    }

    public static boolean hasRoute(String method, String path) {
        return routes.containsKey(method + ":" + path);
    }

    public static void handleRoute(Request req, Response res) throws IOException {
        BiConsumer<Request, Response> handler = routes.get(req.getMethod() + ":" + req.getPath());
        if (handler != null) {
            handler.accept(req, res);
        } else {
            res.send(404, "text/html", "<h1>404 Not Found</h1>");
        }
    }


    // Ejemplo de uso y prueba del servidor
    public static void main(String[] args) throws IOException {

        App.get("/hello", (req, res) -> {
            try {
                res.send(200, "text/plain", "Hello, World!");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        App.post("/app/postTask", (req, res) -> {
            try {
                String body = req.getBody();
                res.send(200, "application/json", "{\"status\":\"Task received: " + body + "\"}");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        HttpServer server = new HttpServer(35000);
        server.setStaticFilesDir("www");
        server.start();
    }
}
