package com.mycompany.ejercicio1;

import java.io.*;
import java.lang.reflect.Method;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.*;
import java.util.function.BiFunction;
import java.util.logging.Level;
import java.util.logging.Logger;

public class HttpServer {


    private static final Map<String, BiFunction<Request, Response, String>> routes = new HashMap<>();
    public static void get(String path, BiFunction<Request, Response, String> handler) {
        routes.put(path, handler);
    }

    private static String staticDirectory = "src/main/resources";

    public static void staticFiles(String folder) {
        if (folder != null && !folder.isBlank()) {
            staticDirectory = folder;
        }
    }

    private static void loadComponents (String[] args){
        try {
            Class c = Class.forName(args[0]);
            if (c.isAnnotationPresent(RestController.class)) {
                Method[] methods = c.getDeclaredMethods();

                for (Method m : methods) {
                    if (m.isAnnotationPresent(GetMapping.class)) {
                        String mapping = m.getAnnotation(GetMapping.class).value();
                    }
                }
            }
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(HttpServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void start(int port, WebRouter router) throws IOException {
        staticFiles("src/main/resources");
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Servidor iniciado en: http://localhost:" + port);


            get("/App/hello", (req, res) -> {
                List<String> names = req.getValues("name");
                return names.isEmpty() ? "Hello world" : "Hello " + String.join(", ", names);
            });
            get("/App/pi", (req, res) -> Double.toString(Math.PI));

            while (true) {
                try (Socket clientSocket = serverSocket.accept()) {
                    handleClient(clientSocket, router);
                }
            }
        }
    }


    // src/main/java/com/mycompany/ejercicio1/HttpServer.java
    private static void handleClient(Socket socket, WebRouter router) {
        try (
                BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                OutputStream out = socket.getOutputStream()
        ) {
            String requestLine = reader.readLine();
            if (requestLine == null || requestLine.trim().isEmpty()) {
                return;
            }

            String[] parts = requestLine.split(" ");
            String method = parts[0];
            String path = parts.length > 1 ? parts[1].split("\\?")[0] : "/";

            String line;
            while ((line = reader.readLine()) != null && !line.isEmpty()) {}

            Request req = new Request(method, path);
            Response res = new Response();

            if ("GET".equalsIgnoreCase(method)) {
                WebRouter.Route route = router.findRoute(path);

                if (route != null) {
                    Object result = router.invoke(route, req);
                    res.setContentType("text/html; charset=UTF-8");
                    res.setBody(result.toString());
                    sendResponse(out, res);
                } else {
                    serveStatic(path, out);
                }
            } else {
                sendResponse(out, res);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static void sendResponse(OutputStream out, Response res) throws IOException {
        byte[] body = res.getBodyBytes();
        String headers =
                "HTTP/1.1 " + res.getStatus() + " OK\r\n" +
                        "Content-Type: " + res.getContentType() + "\r\n" +
                        "Content-Length: " + body.length + "\r\n" +
                        "Connection: close\r\n\r\n";
        out.write(headers.getBytes(StandardCharsets.UTF_8));
        out.write(body);
        out.flush();
    }

    private static void serveStatic(String path, OutputStream out) throws IOException {
        String safePath = path.equals("/") ? "index.html" :
                (path.startsWith("/") ? path.substring(1) : path);

        File file = new File(staticDirectory, safePath);
        Response res = new Response();

        if (!file.exists() || file.isDirectory()) {
            res.setStatus(404);
            res.setBody("404 Not Found");
            sendResponse(out, res);
            return;
        }

        byte[] content = Files.readAllBytes(file.toPath());
        res.setBodyBytes(guessMimeType(safePath), content);
        sendResponse(out, res);
    }

    private static String guessMimeType(String path) {
        String lower = path.toLowerCase(Locale.ROOT);
        if (lower.endsWith(".html") || lower.endsWith(".htm")) return "text/html; charset=UTF-8";
        if (lower.endsWith(".css")) return "text/css; charset=UTF-8";
        if (lower.endsWith(".js")) return "application/javascript; charset=UTF-8";
        if (lower.endsWith(".png")) return "image/png";
        if (lower.endsWith(".jpg") || lower.endsWith(".jpeg")) return "image/jpeg";
        if (lower.endsWith(".gif")) return "image/gif";
        return "application/octet-stream";
    }
}
