package ru.otus.service.db;

import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;
import ru.otus.domain.User;
import ru.otus.repository.UserRepository;

import java.util.List;
import java.util.Optional;

@Service
public class DbServiceImpl implements DbService {
    private final UserRepository repository;

    public DbServiceImpl(UserRepository repository) {
        this.repository = repository;
    }

    @Override
    public ObjectId insert(User user) {
        return repository.insert(user);
    }

    @Override
    public Optional<List<User>> findAll() {
        return repository.findAll();
    }
}
