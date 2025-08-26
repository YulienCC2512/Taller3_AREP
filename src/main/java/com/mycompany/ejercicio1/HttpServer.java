package main.java.com.mycompany.ejercicio1;

import java.io.*;
import java.lang.reflect.Method;
import java.net.*;
import java.nio.file.*;

/**
 * Julian Santiago Cardenas
 */
public class HttpServer {

    private int port;
    private ServerSocket serverSocket;
    private boolean running;
    private String staticFilesDir = "www";

    public HttpServer(int port) {
        this.port = port;
    }

    public void setStaticFilesDir(String dir) {
        this.staticFilesDir = dir;
    }


    public static void main(String[] args) throws IOException, URISyntaxException {
        ServerSocket serverSocket = null;
        private static void loadComponents (Srings args[]){
            try {
                Class c = class.forName(args[0];)
                if (c.isAnnotationPresent(RestController.class)) {
                    Method() Method[] methods = c.getDeclaredMethods();

                    for (Method m : methods) {
                        if (m.isAnnotationPresent(GetMapping.class)) {
                            String mapping = m.getAnnotation(getMapping.class).mapping();
                            services.put(mapping, m);
                        }
                    }
                }
            } catch (IOException e) {
                System.err.println("Could not listen on port: 35000.");
                System.exit(1);
            }
        }
    }
    public void start() throws IOException {
        try {
            serverSocket = new ServerSocket(port);
            running = true;
            System.out.println("Server started on port " + port);
        } catch (IOException e) {
            System.err.println("Could not start server on port " + port);
            throw e;
        }

        while (running) {
            try (Socket clientSocket = serverSocket.accept()) {
                handleClient(clientSocket);
            } catch (IOException | URISyntaxException e) {
                e.printStackTrace();
            }
        }
        serverSocket.close();
    }

    private void handleClient(Socket clientSocket) throws IOException, URISyntaxException {
        BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        PrintWriter out = new PrintWriter(clientSocket.getOutputStream());
        BufferedOutputStream dataOut = new BufferedOutputStream(clientSocket.getOutputStream());


        Request request = Request.build(in);


        Response response = new Response(out, dataOut);


        if (App.hasRoute(request.getMethod(), request.getPath())) {
            App.handleRoute(request, response);
        } else {
            serveStaticFile(request.getPath(), response);
        }

        in.close();
        response.close();
    }

    private void serveStaticFile(String path, Response response) throws IOException {
        if (staticFilesDir == null) {
            response.send(500, "text/html", "<h1>Static files directory not set</h1>");
            return;
        }

        if ("/".equals(path)) {
            path = "/index.html";
        }

        File file = new File(staticFilesDir, path);
        if (!file.exists() || file.isDirectory()) {
            response.send(404, "text/html", "<h1>404 Not Found</h1>");
            return;
        }

        try {
            byte[] fileData = Files.readAllBytes(file.toPath());
            response.send(200, typeOfFile(file.getName()), fileData);
        } catch (IOException e) {
            response.send(500, "text/html", "<h1>500 Internal Server Error</h1>");
        }
    }

    private String typeOfFile(String fileName) {
        if (fileName.endsWith(".html") || fileName.endsWith(".htm")) {
            return "text/html";
        } else if (fileName.endsWith(".css")) {
            return "text/css";
        } else if (fileName.endsWith(".js")) {
            return "application/javascript";
        } else if (fileName.endsWith(".png")) {
            return "image/png";
        } else if (fileName.endsWith(".jpg") || fileName.endsWith(".jpeg")) {
            return "image/jpeg";
        } else if (fileName.endsWith(".gif")) {
            return "image/gif";
        } else {
            return "application/octet-stream";
        }
    }
}
