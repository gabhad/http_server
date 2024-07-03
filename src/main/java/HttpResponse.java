
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;

public class HttpResponse {
    public static void sendOK(PrintWriter out) {
        String httpResponse = "HTTP/1.1 200 OK\r\n\r\n";
        out.print(httpResponse);
        out.flush();
    }

    public static void sendOK(PrintWriter out, String body) {
        String httpResponse = "HTTP/1.1 200 OK\r\nContent-Type: text/plain\r\nContent-Length: " + body.length() + "\r\n\r\n" + body;
        out.print(httpResponse);
        out.flush();
    }

    public static void sendOK(PrintWriter out, File file) throws IOException {
        String httpResponse = "HTTP/1.1 200 OK\r\nContent-Type: application/octet-stream\r\nContent-Length: " + file.length() + "\r\n\r\n";
        out.print(httpResponse);
        out.flush();
        try (BufferedReader filReader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = filReader.readLine())!= null) {
                out.println(line);
            }
        }
    }

    public static void fileCreated(PrintWriter out) {
        String httpResponse = "HTTP/1.1 201 Created\r\n\r\n";
        out.print(httpResponse);
        out.flush();
    }

    public static void sendNotFound(PrintWriter out) {
        String httpResponse = "HTTP/1.1 404 Not Found\r\n\r\n";
        out.print(httpResponse);
        out.flush();
    }
}