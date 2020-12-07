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

    public static ConcurrentLinkedQueue<String> queue = new ConcurrentLinkedQueue<>();

    public static void main(String[] args) {
        int size = Runtime.getRuntime().availableProcessors();
        ExecutorService service = Executors.newFixedThreadPool(size);

        try (ServerSocket server = new ServerSocket(8080)) {
            System.out.println("Server started!");
            while (!server.isClosed()) {
                Socket socket = server.accept();
                System.out.println("Client connected!");
                PrintWriter out = new PrintWriter(socket.getOutputStream());
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                service.execute(() -> {
                    try {
                        String str;
                        StringBuffer request = new StringBuffer();
                        while ((str = in.readLine()) != null) {
                            request.append(str);
                        }
                        System.out.println(request.toString());
                        queue.add(parse(request.toString()));
                        String respond = queue.poll();
                        if (respond != null) {
                            out.write(respond);
                            out.flush();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String parse(String request) {
        String[] items = request.split(",");
        return items[1].substring(8, items[1].length() - 1);
    }
}