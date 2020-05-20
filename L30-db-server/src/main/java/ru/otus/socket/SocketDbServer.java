package ru.otus.socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import ru.otus.messagesystem.Message;
import ru.otus.messagesystem.MsClient;

import java.io.BufferedInputStream;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class SocketDbServer {
    private static final Logger logger = LoggerFactory.getLogger(SocketDbServer.class);
    @Value("${db.server.port}")
    private static int PORT;
    @Autowired
    private MsClient msClient;

    public void start() {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
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
            logger.info("Message {} was received from {}", message.getId(), message.getFrom());
            msClient.handle(message);
        } catch (Exception e) {
            logger.error("error", e);
        }
    }
}
