package ru.otus.socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.messagesystem.Message;

import java.io.BufferedOutputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class SocketMessageClient implements SocketClient {
    private static final Logger logger = LoggerFactory.getLogger(SocketMessageClient.class);
    private final String messageServerHost;
    private final int messageServerPort;

    public SocketMessageClient(String messageServerHost, int messageServerPort) {
        this.messageServerHost = messageServerHost;
        this.messageServerPort = messageServerPort;
    }

    @Override
    public void sendMessage(Message message) {
        logger.info("Message {} was received from {} to {}", message.getId(), message.getFrom(), message.getTo());
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
