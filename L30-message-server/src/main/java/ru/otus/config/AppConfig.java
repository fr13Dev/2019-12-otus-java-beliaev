package ru.otus.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import ru.otus.messagesystem.MessageSystem;
import ru.otus.messagesystem.MessageSystemImpl;
import ru.otus.messagesystem.MsClient;
import ru.otus.messagesystem.MsClientImpl;
import ru.otus.socket.SocketMessageServer;

@Configuration
@ComponentScan
public class AppConfig {
    private static final String FRONTEND_SERVICE_CLIENT_NAME = "frontendService";
    private static final String DATABASE_SERVICE_CLIENT_NAME = "databaseService";

    @Bean(destroyMethod = "dispose")
    public MessageSystem messageSystem() {
        var messageSystem = new MessageSystemImpl();

        var databaseMsClient = databaseMsClient(messageSystem);
        messageSystem.addClient(databaseMsClient);

        var frontendMsClient = frontendMsClient(messageSystem);
        messageSystem.addClient(frontendMsClient);
        return messageSystem;
    }

    @Bean
    public MsClient databaseMsClient(MessageSystem messageSystem) {
        return new MsClientImpl(DATABASE_SERVICE_CLIENT_NAME, messageSystem);
    }

    @Bean
    public MsClient frontendMsClient(MessageSystem messageSystem) {
        return new MsClientImpl(FRONTEND_SERVICE_CLIENT_NAME, messageSystem);
    }

    @Bean
    public SocketMessageServer socketMessageServer() {
        return new SocketMessageServer();
    }
}
