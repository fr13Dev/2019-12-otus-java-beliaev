package ru.otus;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import ru.otus.config.AppConfig;
import ru.otus.socket.SocketMessageServer;

public class MessageServerApp {

    public static void main(String[] args) {
        var context = new AnnotationConfigApplicationContext(AppConfig.class);
        var messageServer = context.getBean(SocketMessageServer.class);
        messageServer.start();
    }
}
