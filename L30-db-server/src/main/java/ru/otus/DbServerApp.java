package ru.otus;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import ru.otus.config.AppConfig;

public class DbServerApp {

    public static void main(String[] args) {
        var context = new AnnotationConfigApplicationContext(AppConfig.class);
    }
}
