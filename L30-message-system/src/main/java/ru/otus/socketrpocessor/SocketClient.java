package ru.otus.socketrpocessor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.messagesystem.Message;

import java.io.BufferedOutputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class SocketClient {
    private static final Logger logger = LoggerFactory.getLogger(SocketClient.class);

    private final String host;
    private final int port;

    public SocketClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public void sendMessage(Message message) {
        logger.info("Message {} was received from {} to {}", message.getId(), message.getFrom(), message.getTo());
        logger.info("connected to the host {} on {} port", host, port);
        try (Socket clientSocket = new Socket(host, port);
             ObjectOutputStream oos = new ObjectOutputStream(new BufferedOutputStream(clientSocket.getOutputStream()))) {
            oos.writeObject(message);
            logger.info("Message {} was sent to {}", message.getId(), message.getTo());
        } catch (Exception e) {
            logger.error("error", e);
        }
    }
}
