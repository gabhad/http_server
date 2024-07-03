
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;


public class HttpRequestHandler implements Runnable  {
    private final Socket clientSocket;
    private String directory;

    public HttpRequestHandler(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }
    public HttpRequestHandler(Socket clientSocket, String directory) {
        this.clientSocket = clientSocket;
        this.directory = directory;
    }

    @Override
    public void run() {
        try(BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)) {
            String requestLine = in.readLine();
            if (requestLine!= null && !requestLine.isEmpty()) {
                Map<String, String> headers = getHeaders(in);
                handleRequest(requestLine, headers, out, in);
            }
        } catch (IOException e) {
            System.err.println("IOException: " + e.getMessage());
         } finally {
            try {
                clientSocket.close();
            } catch (IOException e) {
                System.err.println("IOException while closing resources: " + e.getMessage());
            }
        }
    }

    private Map<String, String> getHeaders(BufferedReader in) throws IOException {
        Map<String, String> headers = new HashMap<>();
        String line = in.readLine();
        while (line!= null &&!line.isEmpty()) {
            String[] parts = line.split(": ");
            if (parts.length == 2)
                headers.put(parts[0], parts[1]);
            line = in.readLine();
        }
        return headers;
    }

    private void handleRequest(String requestLine, Map<String, String> headers, PrintWriter out, BufferedReader in) throws IOException {
        String[] requestParts = requestLine.split(" ");
        if (requestParts.length == 3) {
            String method = requestParts[0];
            String path = requestParts[1];

            if (method.equalsIgnoreCase("GET"))
                handleGetRequest(path, headers, out);
            else if (method.equalsIgnoreCase("POST")) {
                int contentLength = Integer.parseInt(headers.getOrDefault("Content-Length", "0"));
                handlePostRequest(path, contentLength, in, out);
            }
            else
                HttpResponse.sendNotFound(out);
        } else
            HttpResponse.sendNotFound(out);
    }

    public void handleGetRequest(String path, Map<String,String> headers, PrintWriter out)
    {
        if (path.equals("/"))
        HttpResponse.sendOK(out);
        else if (path.equals("/user-agent"))
        {
            String userAgent = headers.get("User-Agent");
            HttpResponse.sendOK(out, userAgent);
        }
        else if (path.startsWith("/echo/"))
            HttpResponse.sendOK(out, path.substring(6));
        else if (path.startsWith("/files/") && !this.directory.isEmpty())
            returnFile(out, this.directory + path.substring(6));
        else
            HttpResponse.sendNotFound(out);

    }

    public void handlePostRequest(String path, int contentLength, BufferedReader in, PrintWriter out) throws IOException
    {
        if (path.startsWith("/files/")) {
            String fileName = path.substring(7);
            File file = new File(directory, fileName);

            try (FileOutputStream fileOut = new FileOutputStream(file)) {
                char[] buffer = new char[1024];
                int bytesRead;
                int totalRead = 0;

                while (totalRead < contentLength && (bytesRead = in.read(buffer, 0, Math.min(buffer.length, contentLength - totalRead))) != -1) {
                    fileOut.write(new String(buffer, 0, bytesRead).getBytes());
                    totalRead += bytesRead;
                }
                HttpResponse.fileCreated(out);
            } catch (FileNotFoundException e) {
                HttpResponse.sendNotFound(out);
            }
        } else {
            HttpResponse.sendNotFound(out);
        }
    }

    public void returnFile(PrintWriter out, String path)
    {
        try {
            File file = new File(path);
            if (file.exists() && file.isFile())
                HttpResponse.sendOK(out, file);
            else
                HttpResponse.sendNotFound(out);
        } catch (IOException e) {
            HttpResponse.sendNotFound(out);
        }
    }
}