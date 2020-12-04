package ru.job4j.pooh;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class Subscriber {
    public static void main(String[] args) {
        Subscriber subscriber = new Subscriber();
        System.out.println(subscriber.get());
    }
    public String get() {
        StringBuffer respond = new StringBuffer();
        try (Socket socket = new Socket("localhost", 8080);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))
        ) {
            String str;
            while ((str = in.readLine()) != null) {
                respond.append(str);
            }
            System.out.println(respond.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return respond.toString();
    }
}
