package ru.otus.service;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.springframework.stereotype.Service;

@Service
public class SingleDatabaseMongoClient {
    public static final String USERS_COLLECTION_NAME = "users";
    private static final String DB_URL = "mongodb://localhost";
    private static final String DB_NAME = "mongo-db";

    private final MongoClient client;
    private final MongoDatabase database;

    public SingleDatabaseMongoClient() {
        this.client = MongoClients.create(DB_URL);
        this.database = client.getDatabase(DB_NAME);
        database.drop();
        Runtime.getRuntime().addShutdownHook(new Thread(client::close));
    }

    public MongoCollection<Document> getCollection(String name) {
        return database.getCollection(name);
    }
}
