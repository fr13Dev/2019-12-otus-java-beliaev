package ru.otus.config;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.client.MongoClients;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.otus.service.SingleDatabaseMongoClientImpl;

@Configuration
public class AppConfig {
    @Value("${mongodb.url}")
    private static String MONGODB_URL;
    @Value("${mongodb.db-name}")
    private static String DB_NAME;

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
