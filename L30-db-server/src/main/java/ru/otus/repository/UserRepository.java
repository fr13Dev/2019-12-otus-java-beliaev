package ru.otus.repository;

import org.bson.types.ObjectId;
import ru.otus.domain.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository {

    ObjectId insert(User user);

    Optional<List<User>> findAll();
}
