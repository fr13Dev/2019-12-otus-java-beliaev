package ru.otus.handlers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.common.Serializers;
import ru.otus.messagesystem.Message;
import ru.otus.messagesystem.RequestHandler;
import ru.otus.service.frontend.FrontendService;

import java.util.List;
import java.util.Optional;

public class GetAllUsersResponseHandler implements RequestHandler {
    private static final Logger logger = LoggerFactory.getLogger(GetAllUsersResponseHandler.class);
    private final FrontendService frontendService;

    public GetAllUsersResponseHandler(FrontendService frontendService) {
        this.frontendService = frontendService;
    }

    @Override
    public Optional<Message> handle(Message msg) {
        logger.info("message to get all users: {}", msg);
        try {
            var list = Serializers.deserialize(msg.getPayload(), List.class);
            logger.info("retrieved db users: {}", list);
            var sourceMessageId = msg.getSourceMessageId().orElseThrow(() -> new RuntimeException("not found source message"));
            frontendService.takeConsumer(sourceMessageId, List.class).ifPresent(consumer -> consumer.accept(list));
        } catch (Exception e) {
            logger.error("msg: " + msg, e);
        }
        return Optional.empty();
    }
}
