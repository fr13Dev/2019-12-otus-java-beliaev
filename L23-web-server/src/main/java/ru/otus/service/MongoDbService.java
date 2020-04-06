package ru.otus.service;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import org.bson.Document;

public class MongoDbService {
    private static final String URL = "mongodb://localhost";
    private static final String DB_NAME = "mongo-db";
    private static final String USERS_COLLECTION_NAME = "users";

    private final MongoClient client;
    private final MongoCollection<Document> collection;

    public MongoDbService() {
        client = MongoClients.create(URL);
        var database = client.getDatabase(DB_NAME);
        database.drop();
        this.collection = database.getCollection(USERS_COLLECTION_NAME);
        Runtime.getRuntime().addShutdownHook(new Thread(client::close));
    }

    public MongoCollection<Document> getUsersCollection() {
        return collection;
    }
}
