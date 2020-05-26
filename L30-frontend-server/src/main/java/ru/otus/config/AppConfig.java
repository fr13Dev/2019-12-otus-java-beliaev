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
import ru.otus.socket.SocketFrontendServer;
import ru.otus.socketrpocessor.SocketClient;

@Configuration
@ComponentScan
public class AppConfig {
    @Value("${frontend.name}")
    String frontendName;
    @Value("${frontend.server.port}")
    private int frontendPort;

    @Value("${db.server.name}")
    private String databaseServiceClientName;

    @Value("${message.server.host}")
    private String messageServerHost;
    @Value("${message.server.port}")
    private int messageServerPort;

    @Autowired
    private MsClient frontendMsClient;
    @Autowired
    SocketClient socketClient;

    @Bean
    public MsClient frontendMsClient() {
        var frontendMsClient = new FrontendMsClient(frontendName, socketClient);
        frontendMsClient.addHandler(MessageType.ALL_USERS, new GetAllUsersResponseHandler(frontendService(frontendMsClient)));
        frontendMsClient.addHandler(MessageType.INSERT_USER, new InsertUserResponseHandler(frontendService(frontendMsClient)));
        return frontendMsClient;
    }

    @Bean
    public FrontendService frontendService(MsClient frontendMsClient) {
        return new FrontendServiceImpl(frontendMsClient, databaseServiceClientName);
    }

    @Bean(destroyMethod = "shutdown")
    public SocketFrontendServer socketFrontendServer() {
        return new SocketFrontendServer(frontendPort, frontendMsClient);
    }

    @Bean
    public SocketClient socketFrontendClient() {
        return new SocketClient(messageServerHost, messageServerPort);
    }
}
