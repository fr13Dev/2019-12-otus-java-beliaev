package ru.otus.config;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.client.MongoClients;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import ru.otus.handlers.GetAllUsersRequestHandler;
import ru.otus.handlers.InsertUserRequestHandler;
import ru.otus.messagesystem.DbMsClient;
import ru.otus.messagesystem.MessageType;
import ru.otus.messagesystem.MsClient;
import ru.otus.service.DbService;
import ru.otus.service.SingleDatabaseMongoClientImpl;
import ru.otus.socket.SocketDbClient;

@Configuration
@ComponentScan(basePackages = "ru.otus")
@PropertySource("classpath:application.properties")
public class AppConfig {
    @Value("${mongodb.url}")
    private String mongodbUrl;
    @Value("${mongodb.db-name}")
    private String dbName;

    @Value("${db.server.name}")
    private String dbServerName;

    @Autowired
    private DbService dbService;
    @Autowired
    private SocketDbClient socketDbClient;

    @Bean
    public ObjectMapper objectMapper() {
        var mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return mapper;
    }

    @Bean(initMethod = "dropDatabase", destroyMethod = "close")
    public SingleDatabaseMongoClientImpl mongoClient() {
        var mongoClient = MongoClients.create(mongodbUrl);
        return new SingleDatabaseMongoClientImpl(mongoClient, dbName);
    }

    @Bean
    public MsClient dbMsClient() {
        var dbMsClient = new DbMsClient(dbServerName, socketDbClient);
        dbMsClient.addHandler(MessageType.ALL_USERS, new GetAllUsersRequestHandler(dbService));
        dbMsClient.addHandler(MessageType.INSERT_USER, new InsertUserRequestHandler(dbService));
        return dbMsClient;
    }
}
