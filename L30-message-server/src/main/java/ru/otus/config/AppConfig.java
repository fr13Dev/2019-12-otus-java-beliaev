package ru.otus.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import ru.otus.AppRunner;
import ru.otus.messagesystem.MessageSystem;
import ru.otus.messagesystem.MessageSystemImpl;
import ru.otus.messagesystem.MsClient;
import ru.otus.messagesystem.MsClientImpl;
import ru.otus.socket.SocketMessageServer;
import ru.otus.socketrpocessor.SocketClient;

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

    @Value("${frontend.server.file}")
    private String frontendServerFile;
    @Value("${db.server.file}")
    private String dbServerFile;

    @Value("${message.server.port}")
    private int port;
    @Autowired
    private MessageSystem messageSystem;

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
    public SocketClient dbSocketClient() {
        return new SocketClient(databaseServerHost, databaseServerPort);
    }

    @Bean
    public SocketClient frontendSocketClient() {
        return new SocketClient(frontendServerHost, frontendServerPort);
    }

    @Bean
    public AppRunner appRunner() {
        return new AppRunner(socketMessageServer(), frontendServerFile, dbServerFile);
    }

    @Bean
    public SocketMessageServer socketMessageServer() {
        return new SocketMessageServer(port, messageSystem);
    }
}
