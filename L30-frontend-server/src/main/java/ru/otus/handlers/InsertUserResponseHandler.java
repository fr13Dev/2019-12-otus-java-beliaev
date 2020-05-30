package ru.otus.handlers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.common.Serializers;
import ru.otus.domain.User;
import ru.otus.messagesystem.Message;
import ru.otus.messagesystem.RequestHandler;
import ru.otus.service.FrontendService;

import java.util.Optional;

public class InsertUserResponseHandler implements RequestHandler {
    private static final Logger logger = LoggerFactory.getLogger(InsertUserResponseHandler.class);
    private final FrontendService frontendService;

    public InsertUserResponseHandler(FrontendService frontendService) {
        this.frontendService = frontendService;
    }

    @Override
    public Optional<Message> handle(Message msg) {
        logger.info("message to insert an new user: {}", msg);
        try {
            var user = Serializers.deserialize(msg.getPayload(), User.class);
            logger.info("retrieved db user: {}", user);
            var sourceMessageId = msg.getSourceMessageId().orElseThrow(() -> new RuntimeException("not found source message"));
            frontendService.takeConsumer(sourceMessageId, User.class).ifPresent(consumer -> consumer.accept(user));
        } catch (Exception e) {
            logger.error("msg: " + msg, e);
        }
        return Optional.empty();
    }
}
