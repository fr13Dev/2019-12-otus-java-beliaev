package ru.otus.service;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class SingleDatabaseMongoClient {

    private final MongoClient client;
    private final MongoDatabase database;

    public SingleDatabaseMongoClient(@Value("mongodb://localhost") String dbUrl,
                                     @Value("mongo-db") String dbName) {
        this.client = MongoClients.create(dbUrl);
        this.database = client.getDatabase(dbName);
        Runtime.getRuntime().addShutdownHook(new Thread(client::close));
    }

    public MongoCollection<Document> getCollection(String name) {
        return database.getCollection(name);
    }

    public void dropDatabase() {
        database.drop();
    }
}
