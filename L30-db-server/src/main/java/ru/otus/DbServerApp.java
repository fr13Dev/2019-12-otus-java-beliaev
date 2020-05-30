package ru.otus;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import ru.otus.config.AppConfig;
import ru.otus.socket.SocketDbServer;

public class DbServerApp {

    public static void main(String[] args) {
        var context = new AnnotationConfigApplicationContext(AppConfig.class);
        var dbServer = context.getBean(SocketDbServer.class);
        dbServer.start();
    }
}
