package ru.otus.handlers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.common.Serializers;
import ru.otus.domain.User;
import ru.otus.messagesystem.Message;
import ru.otus.messagesystem.MessageType;
import ru.otus.messagesystem.RequestHandler;
import ru.otus.service.db.DbService;

import java.util.Optional;

public class InsertUserRequestHandler implements RequestHandler {
    private static final Logger logger = LoggerFactory.getLogger(InsertUserRequestHandler.class);
    private final DbService dbService;

    public InsertUserRequestHandler(DbService dbService) {
        this.dbService = dbService;
    }

    @Override
    public Optional<Message> handle(Message msg) {
        logger.info("message to insert an new user: {}", msg);
        var user = Serializers.deserialize(msg.getPayload(), User.class);
        logger.info("new user: {}", user);
        dbService.insert(user);
        return Optional.of(new Message(
                msg.getTo(),
                msg.getFrom(),
                msg.getId(),
                MessageType.INSERT_USER.getValue(),
                Serializers.serialize(user)));
    }
}
