package ru.otus.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import ru.otus.handlers.GetAllUsersResponseHandler;
import ru.otus.handlers.InsertUserResponseHandler;
import ru.otus.messagesystem.FrontendMsClient;
import ru.otus.messagesystem.MessageType;
import ru.otus.messagesystem.MsClient;
import ru.otus.service.FrontendService;
import ru.otus.service.FrontendServiceImpl;
import ru.otus.socket.SocketFrontendClient;

@Configuration
@ComponentScan
public class AppConfig {
    @Value("${frontend.name}")
    String frontendName;
    @Autowired
    SocketFrontendClient socketFrontendClient;

    @Bean
    public MsClient frontendMsClient() {
        var frontendMsClient = new FrontendMsClient(frontendName, socketFrontendClient);
        frontendMsClient.addHandler(MessageType.ALL_USERS, new GetAllUsersResponseHandler(frontendService(frontendMsClient)));
        frontendMsClient.addHandler(MessageType.INSERT_USER, new InsertUserResponseHandler(frontendService(frontendMsClient)));
        return frontendMsClient;
    }

    @Bean
    public FrontendService frontendService(MsClient frontendMsClient) {
        return new FrontendServiceImpl(frontendMsClient);
    }
}
