package ru.otus.dao;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import org.bson.Document;
import org.bson.types.ObjectId;
import ru.otus.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class MongoUserDao implements UserDao {
    private static final TypeReference<Map<String, Object>> STR_OBJECT_MAP_TYPE_REF = new TypeReference<>() {};
    private static final String ID_DOC_ATTR = "_id";
    private static final String LOGIN_DOC_ATTR = "login";
    private static final String PRIVILEGED_DOC_ATTR = "privileged";

    private final MongoCollection<Document> collection;
    private final ObjectMapper mapper;

    public MongoUserDao(MongoCollection<Document> collection, ObjectMapper mapper) {
        this.collection = collection;
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
    public Optional<User> findByLogin(String login) {
        var query = Filters.eq(LOGIN_DOC_ATTR, login);
        var document = collection.find(query).first();
        return Optional.of(document).map(this::document2User);
    }

    @Override
    public Optional<List<User>> findAll() {
        var query = Filters.all(PRIVILEGED_DOC_ATTR, false);
        var documents = collection.find(query);
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
}
