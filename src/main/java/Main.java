import java.io.File;

public class Main {
  public static void main(String[] args) {
    HttpServer server;
    if (args.length == 2 && args[0].equals("--directory") && !args[1].isEmpty()) {
      String directory = args[1];
      server = new HttpServer(4221, directory);
      File dir = new File(directory);
      if (!dir.exists()) {
        if (dir.mkdirs()) {
            System.out.println("Directory created: " + directory);
        } else {
            System.err.println("Failed to create directory: " + directory);
            return;
        }
      }
    }
    else
      server = new HttpServer(4221);
    server.start();
  }
}