package ru.otus;

import ru.otus.dao.InMemoryUserDao;
import ru.otus.server.UserWebServerImpl;
import ru.otus.service.TemplateProcessorImpl;
import ru.otus.service.UserAuthServiceImpl;

public class Main {
    private static final int WEB_SERVER_PORT = 8080;
    private static final String TEMPLATES_DIR = "/templates/";

    public static void main(String[] args) throws Exception {
        var userDao = new InMemoryUserDao();
        var authService = new UserAuthServiceImpl(userDao);
        var templateProcessor = new TemplateProcessorImpl(TEMPLATES_DIR);
        var server = new UserWebServerImpl(userDao, authService, templateProcessor, WEB_SERVER_PORT);
        server.start();
        server.join();
    }
}
