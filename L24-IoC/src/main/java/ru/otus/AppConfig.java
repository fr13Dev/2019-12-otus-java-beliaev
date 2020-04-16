package ru.otus;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.client.MongoClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.otus.service.SingleDatabaseMongoClientImpl;

@Configuration
public class AppConfig {
    private static final String MONGODB_URL = "mongodb://localhost";
    private static final String DB_NAME = "mongo-db";

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
}
