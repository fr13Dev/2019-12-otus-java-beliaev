package ru.otus.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.otus.domain.User;
import ru.otus.messagesystem.MessageType;
import ru.otus.messagesystem.MsClient;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

@Service
public class FrontendServiceImpl implements FrontendService {
    private static final Logger logger = LoggerFactory.getLogger(FrontendServiceImpl.class);
    private final Map<UUID, Consumer<?>> consumerMap = new ConcurrentHashMap<>();
    private final MsClient msClient;
    @Value("${db.server.name}")
    private String databaseServiceClientName;

    public FrontendServiceImpl(MsClient msClient) {
        this.msClient = msClient;
    }

    @Override
    public void insert(User user, Consumer<User> dataConsumer) {
        var outMsg = msClient.produceMessage(databaseServiceClientName, user, MessageType.INSERT_USER);
        consumerMap.put(outMsg.getId(), dataConsumer);
        msClient.sendMessage(outMsg);
    }

    @Override
    public void findAll(Consumer<List<User>> dataConsumer) {
        var outMsg = msClient.produceMessage(databaseServiceClientName, null, MessageType.ALL_USERS);
        consumerMap.put(outMsg.getId(), dataConsumer);
        msClient.sendMessage(outMsg);
    }

    @Override
    public <T> Optional<Consumer<T>> takeConsumer(UUID sourceMessageId, Class<T> tClass) {
        Consumer<T> consumer = (Consumer<T>) consumerMap.remove(sourceMessageId);
        if (consumer == null) {
            logger.warn("consumer not found for: {}", sourceMessageId);
            return Optional.empty();
        }
        return Optional.of(consumer);
    }
}
