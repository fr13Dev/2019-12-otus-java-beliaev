package ru.otus.repository;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import ru.otus.domain.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
@Qualifier("inMemoryRepository")
public class InMemoryUserRepository implements UserRepository {
    private final List<User> users = new ArrayList<>();

    {
        users.add(new User("admin", "admin", "admin"));
        users.add(new User("admin2", "admin2", "admin2"));
    }

    @Override
    public Long insert(User user) {
        users.add(user);
        return 1L;
    }

    @Override
    public Optional<User> findByLogin(String login) {
        return Optional.of(new User("admin", "admin", "admin"));
    }

    @Override
    public Optional<List<User>> findAll() {
        return Optional.of(users);
    }
}
