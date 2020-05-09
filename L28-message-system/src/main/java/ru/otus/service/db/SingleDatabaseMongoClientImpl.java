package ru.otus.service.db;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

public class SingleDatabaseMongoClientImpl implements SingleDatabaseMongoClient {
    private final MongoClient client;
    private final MongoDatabase database;

    public SingleDatabaseMongoClientImpl(MongoClient mongoClient, String dbName) {
        this.client = mongoClient;
        this.database = client.getDatabase(dbName);
    }

    @Override
    public MongoCollection<Document> getCollection(String name) {
        return database.getCollection(name);
    }

    @Override
    public void dropDatabase() {
        database.drop();
    }

    @Override
    public void close() {
        client.close();
    }
}
