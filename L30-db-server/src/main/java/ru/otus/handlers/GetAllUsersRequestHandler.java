package ru.otus.handlers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.common.Serializers;
import ru.otus.messagesystem.Message;
import ru.otus.messagesystem.MessageType;
import ru.otus.messagesystem.RequestHandler;
import ru.otus.service.DbService;

import java.util.Collections;
import java.util.Optional;


public class GetAllUsersRequestHandler implements RequestHandler {
    private static final Logger logger = LoggerFactory.getLogger(GetAllUsersRequestHandler.class);
    private final DbService dbService;

    public GetAllUsersRequestHandler(DbService dbService) {
        this.dbService = dbService;
    }

    @Override
    public Optional<Message> handle(Message msg) {
        logger.info("message to get all users: {}", msg);
        var users = dbService.findAll().orElse(Collections.emptyList());
        logger.info("retrieved db users: {}", users);
        return Optional.of(new Message(
                msg.getTo(),
                msg.getFrom(),
                msg.getId(),
                MessageType.ALL_USERS.getValue(),
                Serializers.serialize(users)));
    }
}
