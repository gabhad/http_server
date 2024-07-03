# Java HTTP Server

This project implements a simple HTTP server in Java. The server can handle GET and POST requests and supports functionalities such as displaying the user agent and managing files in a specified directory.

## Features

- Responds to HTTP GET requests for paths `/`, `/user-agent`, `/echo/<message>`, and `/files/<filename>`.
- Responds to HTTP POST requests to create files in a specified directory.
- Can be launched with a `--directory` option to specify a directory where files will be created and read.

## Project Structure

- **src/main/java**: Contains the Java source files.
  - `Main.java`: The main entry point of the server.
  - `HttpServer.java`: The main HTTP server class.
  - `HttpRequestHandler.java`: Handles individual HTTP requests.
  - `HttpResponse.java`: Utility class for sending HTTP responses.
  
- **run_server.sh**: Script to compile and run the server easily.

## Prerequisites

- Java Development Kit (JDK) 8 or higher

## Compilation and Execution

### Using the `run_server.sh` Script

The `run_server.sh` script compiles the source files and runs the server with the provided arguments.

1. Make the script executable:

   ```bash
   chmod +x run_server.sh
   ```

2. Run the script with the necessary arguments:
      ```bash
   ./run_server.sh [--directory <directory>]
     ```
- `--directory <directory>`: (Optional) Specifies a directory where files will be created and read. If this directory does not exist, it will be created automatically.

## Example Command

- Start the server without specifying a directory:

```bash
./run_server.sh
```

- Start the server and specify a directory for files:

```bash
./run_server.sh --directory /tmp/
```

## Detailed Features

# GET Requests

- **GET /**: Responds with an "OK" message.
- **GET /user-agent**: Responds with the value of the User-Agent header of the request.
- **GET /echo/<message>**: Responds with the specified message.
- **GET /files/<filename>**: Responds with the contents of the specified file in the defined directory.

# POST Requests

- **POST /files/<filename>**: Creates an empty file with the specified name in the defined directory.

## Notes

- The server listens on port 4221.
- Requests must be well-formed according to the HTTP/1.1 protocol.

## Example Test

To test the server, you can use tools like `curl`.

- To test a GET request:

``` bash

curl -v http://localhost:4221/
```

- To test a POST request to create a file:

```bash
curl -v -X POST http://localhost:4221/files/testfile
```

Ensure the server is running before sending requests.
