package ru.otus.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import ru.otus.domain.User;
import ru.otus.service.frontend.FrontendService;

import java.util.List;

@Controller
public class UserWebSocketController {
    private static final Logger log = LoggerFactory.getLogger(UserWebSocketController.class);
    private final FrontendService frontendService;
    private final SimpMessagingTemplate simpMessagingTemplate;

    public UserWebSocketController(FrontendService frontendService, SimpMessagingTemplate simpMessagingTemplate) {
        this.frontendService = frontendService;
        this.simpMessagingTemplate = simpMessagingTemplate;
    }

    @MessageMapping({"/connect", "/getUsers"})
    public void getUsers() {
        log.info("processing of getting all users");
        frontendService.findAll(users -> {
            log.info("db users for sending: {}", users);
            simpMessagingTemplate.convertAndSend("/topic/users", users);
        });
    }

    @MessageMapping({"/saveUser"})
    public void saveUser(User user) {
        log.info("processing of saving an new user");
        log.info("given user: {}", user);
        frontendService.insert(user, dbUser -> {
            log.info("db user for sending: {}", dbUser);
            simpMessagingTemplate.convertAndSend("/topic/users", List.of(dbUser));
        });
    }
}
