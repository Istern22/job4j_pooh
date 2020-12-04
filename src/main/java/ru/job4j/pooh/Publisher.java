package ru.job4j.pooh;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class Publisher {

    public static void main(String[] args) {
        Publisher publisher = new Publisher();
        publisher.send("weather", "temperature +18C");
        publisher.send("weather", "temperature +17C");
        publisher.send("weather", "temperature +16C");
        publisher.send("weather", "temperature +15C");
    }

    public void send(String queueName, String text) {
        try (Socket socket = new Socket("localhost", 8080);
             PrintWriter out = new PrintWriter(socket.getOutputStream())) {
            out.println(createRequest(queueName, text));
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String createRequest(String queueName, String text) {
        StringBuilder request = new StringBuilder();
        request.append("POST / HTTP/1.1 ").append(System.lineSeparator())
                .append("{ ").append(System.lineSeparator())
                .append("queue").append(" : ").append(queueName).append(", ").append(System.lineSeparator())
                .append("text").append(" : ").append(text).append(System.lineSeparator())
                .append(" }");
        return request.toString();
    }
}
