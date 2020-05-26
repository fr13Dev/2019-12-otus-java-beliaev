package ru.otus.socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.messagesystem.Message;
import ru.otus.messagesystem.MsClient;

import java.io.BufferedInputStream;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SocketFrontendServer {
    private static final Logger logger = LoggerFactory.getLogger(SocketFrontendServer.class);

    private final int port;
    private final MsClient msClient;
    private final ExecutorService clientSocketProcessor = Executors.newSingleThreadExecutor(runnable -> {
        var thread = new Thread(runnable);
        thread.setName("client-socket-processor-thread");
        return thread;
    });

    public SocketFrontendServer(int port, MsClient msClient) {
        this.port = port;
        this.msClient = msClient;
    }

    public void start() {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            logger.info("socket server has started on port {}", port);
            while (!Thread.currentThread().isInterrupted()) {
                logger.info("waiting for client connection");
                try (Socket clientSocket = serverSocket.accept()) {
                    clientSocketProcessor.submit(new ClientSocketProcessor(clientSocket));
                }
            }
        } catch (Exception ex) {
            logger.error("error", ex);
        }
    }

    public void shutdown() {
        logger.info("client socket processor is shutting down");
        clientSocketProcessor.shutdown();
    }

    private class ClientSocketProcessor implements Runnable {
        private final Socket socket;

        public ClientSocketProcessor(Socket socket) {
            logger.info("init client socket processor");
            this.socket = socket;
        }

        @Override
        public void run() {
            try (ObjectInputStream ois = new ObjectInputStream(new BufferedInputStream(socket.getInputStream()))) {
                Message message = (Message) ois.readObject();
                logger.info("Message {} was received from {}", message.getId(), message.getFrom());
                msClient.handle(message);
            } catch (Exception e) {
                logger.error("error", e);
            }
        }
    }
}
