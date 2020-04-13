package ru.otus.repository;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import ru.otus.domain.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
@Qualifier("mongoRepository")
public class MongoUserRepository implements UserRepository {
    private static final TypeReference<Map<String, Object>> STR_OBJECT_MAP_TYPE_REF = new TypeReference<>() {
    };
    private static final String ID_DOC_ATTR = "_id";
    private static final String DB_NAME = "mongo-db";
    private static final String USERS_COLLECTION_NAME = "users";

    private final MongoClient client;
    private final MongoCollection<Document> collection;
    private final ObjectMapper mapper;

    public MongoUserRepository(MongoClient client, ObjectMapper mapper) {
        this.client = client;
        this.collection = getUsersCollection();
        this.mapper = mapper;
    }

    @Override
    public ObjectId insert(User user) {
        var document = new Document(mapper.convertValue(user, STR_OBJECT_MAP_TYPE_REF));
        document.remove(ID_DOC_ATTR);
        collection.insertOne(document);
        return (ObjectId) document.get(ID_DOC_ATTR);
    }

    @Override
    public Optional<List<User>> findAll() {
        var documents = collection.find(Document.parse("{}"));
        var result = new ArrayList<User>();
        var cursor = documents.cursor();
        while (cursor.hasNext()) {
            var doc = cursor.next();
            result.add(document2User(doc));
        }
        return Optional.of(result);
    }

    private User document2User(Document document) {
        document.remove(ID_DOC_ATTR);
        var id = document.get("id");
        document.put("id", id.toString());
        try {
            return mapper.reader().forType(User.class).readValue(document.toJson());
        } catch (JsonProcessingException e) {
            throw new IllegalStateException(e);
        }
    }

    private MongoCollection<Document> getUsersCollection() {
        return client.getDatabase(DB_NAME).getCollection(USERS_COLLECTION_NAME);
    }
}
