package org.example;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

class Server {
    private final int port;

    public Server(int port) {
        this.port = port;
    }

    public void startWithVirtualThreads() {
        try (
                ServerSocket serverSocket = new ServerSocket(port);
                ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor();) {
            System.out.println("Server started on port " + port);
            while (true) {
                Socket clientSocket = serverSocket.accept();
                executor.execute(() -> {
                    String ipAddress = clientSocket.getInetAddress() + ":" + clientSocket.getPort();
                    System.out.println("Client from IP: " + ipAddress + " connected");
                    handleClient(clientSocket);
                    System.out.println("Client from IP: " + ipAddress + " finished its request");
                });
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void handleClient(Socket clientSocket) {
        try (PrintWriter output = new PrintWriter(clientSocket.getOutputStream(), true)) {
            HttpRequest parser = new HttpRequest(clientSocket.getInputStream());

            System.out.println(parser.readBodyAsString());

            HttpResponse response = HttpResponse.builder()
                    .status(HttpStatus.OK)
                    .header("Content-Type", "text/plain; charset=UTF-8")
                    .body("Hello, World! This is a simple HTTP server.".getBytes())
                    .build();

            output.println(response.toString());
        } catch (IOException e) {
            System.err.println("Error handling client: " + e.getMessage());
        }
    }
}
