package main.java.com.mycompany.ejercicio1;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class Response {
    private PrintWriter out;
    private BufferedOutputStream dataOut;

    public Response(PrintWriter out, BufferedOutputStream dataOut) {
        this.out = out;
        this.dataOut = dataOut;
    }

    public void send(int status, String contentType, String content) throws IOException {
        byte[] data = content.getBytes(StandardCharsets.UTF_8);
        send(status, contentType, data);
    }

    public void send(int status, String contentType, byte[] data) throws IOException {
        out.println("HTTP/1.1 " + status + " " );
        out.println("Content-Type: " + contentType);
        out.println("Content-Length: " + data.length);
        out.println();
        out.flush();

        dataOut.write(data, 0, data.length);
        dataOut.flush();
    }

    public void close() throws IOException {
        out.close();
        dataOut.close();
    }
}
