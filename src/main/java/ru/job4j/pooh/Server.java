package ru.job4j.pooh;

import com.google.gson.Gson;

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
                        StringBuffer buffer = new StringBuffer();
                        while (!(str = in.readLine()).isEmpty()) {
                            buffer.append(str).append(System.lineSeparator());
                        }
                        System.out.println("Вывод строки");
                        System.out.println(buffer.toString());
                        String message = parse(buffer.toString());
                        System.out.println("Сообщение: " + message);
                        queue.add(message);
                        System.out.println("Добавлено в очередь");
                        String respond = queue.poll();
                        out.write(respond);
                        System.out.println("Ответ " + respond + " отправлен");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String parse(String str) {
        String[] items = str.split("=");
        return items[1].substring(0, items[1].indexOf(" "));
    }
}