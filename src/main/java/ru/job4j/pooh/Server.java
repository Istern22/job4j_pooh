package ru.job4j.pooh;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {

    public static ConcurrentLinkedQueue queue = new ConcurrentLinkedQueue();

    public static void main(String[] args) {
        int size = Runtime.getRuntime().availableProcessors();
        ExecutorService service = Executors.newFixedThreadPool(size);

        try (ServerSocket server = new ServerSocket(8080)) {
            System.out.println("Server started!");
            while (!server.isClosed()) {
                Socket socket = server.accept();
                System.out.println("Client connected!");
                PrintWriter out = new PrintWriter(socket.getOutputStream());
                System.out.println("Out created!");
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                System.out.println("In created!");
                service.execute(() -> {
                    try {
                        String entry = in.readLine();
                        System.out.println("READ from client message - " + entry);
                        queue.add(entry);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
