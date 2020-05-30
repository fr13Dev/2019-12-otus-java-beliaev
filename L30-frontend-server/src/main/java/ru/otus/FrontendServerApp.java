package ru.otus;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import ru.otus.socket.SocketFrontendServer;

@SpringBootApplication
public class FrontendServerApp implements CommandLineRunner {
    @Autowired
    SocketFrontendServer socketFrontendServer;

    public static void main(String[] args) {
        SpringApplication.run(FrontendServerApp.class);
    }

    @Override
    public void run(String... args) {
        socketFrontendServer.start();
    }
}
