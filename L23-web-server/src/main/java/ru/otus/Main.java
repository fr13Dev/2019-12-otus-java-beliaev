package ru.otus;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import ru.otus.dao.MongoUserDao;
import ru.otus.dao.UserDao;
import ru.otus.model.User;
import ru.otus.server.UserWebServerImpl;
import ru.otus.service.MongoDbService;
import ru.otus.service.TemplateProcessorImpl;
import ru.otus.service.UserAuthServiceImpl;

public class Main {
    private static final int WEB_SERVER_PORT = 8080;
    private static final String TEMPLATES_DIR = "/templates/";

    public static void main(String[] args) throws Exception {
        var mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        var mongoDbService = new MongoDbService();
        var userDao = new MongoUserDao(mongoDbService.getUsersCollection(), mapper);
        var authService = new UserAuthServiceImpl(userDao);
        var templateProcessor = new TemplateProcessorImpl(TEMPLATES_DIR);
        var server = new UserWebServerImpl(userDao, authService, templateProcessor, WEB_SERVER_PORT);

        addDefaultUser(userDao);
        server.start();
        server.join();
    }

    private static void addDefaultUser(UserDao userDao) {
        var admin = "admin";
        var defaultUser = new User(admin, admin, admin, true);
        userDao.insert(defaultUser);
    }
}
