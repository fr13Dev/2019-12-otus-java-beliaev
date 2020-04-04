package ru.otus.dao;

import ru.otus.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class InMemoryUserDao implements UserDao {
    private final List<User> users = new ArrayList<>();

    {
        users.add(new User(1L, "admin", "admin", "admin"));
        users.add(new User(1L, "admin2", "admin2", "admin2"));
    }

    @Override
    public long insert(User user) {
        users.add(user);
        return 1L;
    }

    @Override
    public Optional<User> findByLogin(String login) {
        return Optional.of(new User(1L, "admin", "admin", "admin"));
    }

    @Override
    public Optional<List<User>> getAllUsers() {
        return Optional.of(users);
    }
}
