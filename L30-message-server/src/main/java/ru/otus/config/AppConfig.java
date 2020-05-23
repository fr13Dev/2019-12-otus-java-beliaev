package ru.otus.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import ru.otus.messagesystem.MessageSystem;
import ru.otus.messagesystem.MessageSystemImpl;
import ru.otus.messagesystem.MsClient;
import ru.otus.messagesystem.MsClientImpl;
import ru.otus.socket.SocketMessageClient;

@Configuration
@ComponentScan(basePackages = "ru.otus")
@PropertySource("classpath:application.properties")
public class AppConfig {
    @Value("${frontend.name}")
    private String frontendServiceClientName;
    @Value("${db.server.name}")
    private String databaseServiceClientName;

    @Value("${db.server.host}")
    private String databaseServerHost;
    @Value("${db.server.port}")
    private int databaseServerPort;

    @Value("${frontend.server.host}")
    private String frontendServerHost;
    @Value("${frontend.server.port}")
    private int frontendServerPort;

    @Bean(destroyMethod = "dispose")
    public MessageSystem messageSystem() {
        var messageSystem = new MessageSystemImpl();

        var databaseMsClient = databaseMsClient();
        messageSystem.addClient(databaseMsClient);

        var frontendMsClient = frontendMsClient();
        messageSystem.addClient(frontendMsClient);
        return messageSystem;
    }

    @Bean
    public MsClient databaseMsClient() {
        return new MsClientImpl(databaseServiceClientName, dbSocketClient());
    }

    @Bean
    public MsClient frontendMsClient() {
        return new MsClientImpl(frontendServiceClientName, frontendSocketClient());
    }

    @Bean
    public SocketMessageClient dbSocketClient() {
        return new SocketMessageClient(databaseServerHost, databaseServerPort);
    }

    @Bean
    public SocketMessageClient frontendSocketClient() {
        return new SocketMessageClient(frontendServerHost, frontendServerPort);
    }
}
