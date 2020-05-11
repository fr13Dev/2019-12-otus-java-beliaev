package ru.otus.config;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.client.MongoClients;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import ru.otus.handlers.GetAllUsersRequestHandler;
import ru.otus.handlers.GetAllUsersResponseHandler;
import ru.otus.handlers.InsertUserRequestHandler;
import ru.otus.handlers.InsertUserResponseHandler;
import ru.otus.messagesystem.*;
import ru.otus.service.db.DbService;
import ru.otus.service.db.SingleDatabaseMongoClientImpl;
import ru.otus.service.frontend.FrontendService;
import ru.otus.service.frontend.FrontendServiceImpl;

import javax.annotation.PostConstruct;

@Configuration
@ComponentScan
public class AppConfig {
    private static final String MONGODB_URL = "mongodb://localhost";
    private static final String DB_NAME = "mongo-db";
    private static final String FRONTEND_SERVICE_CLIENT_NAME = "frontendService";
    private static final String DATABASE_SERVICE_CLIENT_NAME = "databaseService";

    @Autowired
    private DbService dbService;

    @Bean
    public ObjectMapper objectMapper() {
        var mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return mapper;
    }

    @Bean(initMethod = "dropDatabase", destroyMethod = "close")
    public SingleDatabaseMongoClientImpl mongoClient() {
        var mongoClient = MongoClients.create(MONGODB_URL);
        return new SingleDatabaseMongoClientImpl(mongoClient, DB_NAME);
    }

    @Bean(destroyMethod = "dispose")
    public MessageSystem messageSystem() {
        return new MessageSystemImpl();
    }

    @Bean
    public MsClient databaseMsClient() {
        return new MsClientImpl(DATABASE_SERVICE_CLIENT_NAME, messageSystem());
    }

    @Bean
    public MsClient frontendMsClient() {
        return new MsClientImpl(FRONTEND_SERVICE_CLIENT_NAME, messageSystem());
    }

    @Bean
    public FrontendService frontendService() {
        return new FrontendServiceImpl(frontendMsClient(), DATABASE_SERVICE_CLIENT_NAME);
    }

    @PostConstruct
    private void postConstruct() {
        var databaseMsClient = databaseMsClient();
        databaseMsClient.addHandler(MessageType.ALL_USERS, new GetAllUsersRequestHandler(dbService));
        databaseMsClient.addHandler(MessageType.INSERT_USER, new InsertUserRequestHandler(dbService));

        var frontendMsClient = frontendMsClient();
        frontendMsClient.addHandler(MessageType.ALL_USERS, new GetAllUsersResponseHandler(frontendService()));
        frontendMsClient.addHandler(MessageType.INSERT_USER, new InsertUserResponseHandler(frontendService()));

        var messageSystem = messageSystem();
        messageSystem.addClient(databaseMsClient);
        messageSystem.addClient(frontendMsClient);
    }
}
