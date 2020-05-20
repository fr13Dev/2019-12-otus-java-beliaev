package ru.otus.service;

import com.mongodb.client.MongoCollection;
import org.bson.Document;

public interface SingleDatabaseMongoClient {

    MongoCollection<Document> getCollection(String name);

    void dropDatabase();

    void close();
}
