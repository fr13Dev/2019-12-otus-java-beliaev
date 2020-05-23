package ru.otus.socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.otus.messagesystem.Message;

import java.io.BufferedOutputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

@Component
public class SocketDbClient {
    private static final Logger logger = LoggerFactory.getLogger(SocketDbClient.class);

    @Value("${message.server.host}")
    private String messageServerHost;
    @Value("${message.server.port}")
    private int messageServerPort;

    public void sendMessage(Message message) {
        logger.info("Message {} was received from {}", message.getId(), message.getFrom());
        logger.info("connected to the host {} on {} port", messageServerHost, messageServerPort);
        try (Socket clientSocket = new Socket(messageServerHost, messageServerPort);
             ObjectOutputStream oos = new ObjectOutputStream(new BufferedOutputStream(clientSocket.getOutputStream()))) {
            oos.writeObject(message);
            logger.info("Message {} was sent to {}", message.getId(), message.getTo());
        } catch (Exception e) {
            logger.error("error", e);
        }
    }
}
