package ru.otus.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import ru.otus.handlers.GetAllUsersResponseHandler;
import ru.otus.handlers.InsertUserResponseHandler;
import ru.otus.messagesystem.*;
import ru.otus.service.FrontendService;
import ru.otus.service.FrontendServiceImpl;
import ru.otus.socket.SocketFrontendClient;

@Configuration
@ComponentScan
public class AppConfig {
    private static final String FRONTEND_SERVICE_CLIENT_NAME = "frontendService";

    @Autowired
    SocketFrontendClient socketFrontendClient;

    @Bean(destroyMethod = "dispose")
    public MessageSystem messageSystem() {
        var messageSystem = new MessageSystemImpl();

        //var frontendMsClient = frontendMsClient(messageSystem);
        //messageSystem.addClient(frontendMsClient);
        return messageSystem;
    }

    @Bean
    public MsClient frontendMsClient() {
        var frontendMsClient = new FrontendMsClient(socketFrontendClient);
        frontendMsClient.addHandler(MessageType.ALL_USERS, new GetAllUsersResponseHandler(frontendService(frontendMsClient)));
        frontendMsClient.addHandler(MessageType.INSERT_USER, new InsertUserResponseHandler(frontendService(frontendMsClient)));
        return frontendMsClient;
    }

    @Bean
    public FrontendService frontendService(MsClient frontendMsClient) {
        return new FrontendServiceImpl(frontendMsClient);
    }
}
