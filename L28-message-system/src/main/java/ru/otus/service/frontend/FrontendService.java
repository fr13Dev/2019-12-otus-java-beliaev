package ru.otus.service.frontend;

import ru.otus.domain.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;

public interface FrontendService {

    void insert(User user, Consumer<User> dataConsumer);

    void findAll(Consumer<List<User>> dataConsumer);

    <T> Optional<Consumer<T>> takeConsumer(UUID sourceMessageId, Class<T> tClass);
}
