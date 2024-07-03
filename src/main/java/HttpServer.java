
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class HttpServer {
    private final int port;
    private String directory;

    public HttpServer(int port) {
        this.port = port;
    }
    public HttpServer(int port, String directory) {
        this.port = port;
        this.directory = directory;
    }

    public void start() {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            serverSocket.setReuseAddress(true);
            System.out.println("Server started on port " + port);

            while(true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Accepted new connection");
                Thread thread;
                if (directory != null)
                    thread = new Thread(new HttpRequestHandler(clientSocket, directory));
                else
                    thread = new Thread(new HttpRequestHandler(clientSocket));
                thread.start();
            }
                
        } catch (IOException e) {
            System.err.println("IOException: " + e.getMessage());
        }
    }
    
}