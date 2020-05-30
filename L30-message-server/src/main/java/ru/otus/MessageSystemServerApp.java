package ru.otus;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import ru.otus.config.AppConfig;

import java.io.IOException;

public class MessageSystemServerApp {

    public static void main(String[] args) throws IOException {
        var context = new AnnotationConfigApplicationContext(AppConfig.class);
        var appRunner = context.getBean(AppRunner.class);
        appRunner.run();
    }
}
