package ru.otus.socket;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.messagesystem.Message;
import ru.otus.messagesystem.MessageSystem;

import java.io.BufferedInputStream;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class SocketMessageServer {
    private static final Logger logger = LoggerFactory.getLogger(SocketMessageServer.class);

    private final int port;
    private final MessageSystem messageSystem;

    public SocketMessageServer(int port, MessageSystem messageSystem) {
        this.port = port;
        this.messageSystem = messageSystem;
    }

    public void start() {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            logger.info("socket server has started on port {}", port);
            while (!Thread.currentThread().isInterrupted()) {
                logger.info("waiting for client connection");
                try (Socket clientSocket = serverSocket.accept()) {
                    clientHandler(clientSocket);
                }
            }
        } catch (Exception ex) {
            logger.error("error", ex);
        }
    }

    private void clientHandler(Socket clientSocket) {
        try (ObjectInputStream ois = new ObjectInputStream(new BufferedInputStream(clientSocket.getInputStream()))) {
            Message message = (Message) ois.readObject();
            logger.info("Message {} was received from {} to {}", message.getId(), message.getFrom(), message.getTo());
            messageSystem.newMessage(message);
        } catch (Exception e) {
            logger.error("error", e);
        }
    }
}
