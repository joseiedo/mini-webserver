package org.example;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;


public class Main {
    static class Server {
        private final int port;

        public Server(int port) {
            this.port = port;
        }

        public void start() {
            try (var serverSocket = new ServerSocket(port)) {
                System.out.println("Server started on port " + port);
                while (true) {
                    Socket clientSocket = serverSocket.accept();
                    handleClient(clientSocket);
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        private void handleClient(Socket clientSocket) {
            try (PrintWriter output = new PrintWriter(clientSocket.getOutputStream(), true)) {
                HttpParser parser = new HttpParser(clientSocket.getInputStream());

                System.out.println(parser.readBodyAsString());

                // Send a basic HTTP response
                output.println("HTTP/1.1 200 OK");
                output.println("Content-Type: text/plain; charset=UTF-8");
                output.println();
                output.println("Hello, World! This is a simple HTTP server.");
            } catch (IOException e) {
                System.err.println("Error handling client: " + e.getMessage());
            }
        }
    }

    public static void main(String[] args) throws IOException {
        Server server = new Server(8080);
        server.start();
    }
}