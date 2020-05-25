package ru.otus;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.socket.SocketMessageServer;

import java.io.File;
import java.io.IOException;

public class AppRunner {
    private static final Logger logger = LoggerFactory.getLogger(AppRunner.class);

    private final SocketMessageServer socketMessageServer;
    private final String frontendServerFile;
    private final String dbServerFile;

    public AppRunner(SocketMessageServer socketMessageServer, String frontendServerFile, String dbServerFile) {
        this.socketMessageServer = socketMessageServer;
        this.frontendServerFile = frontendServerFile;
        this.dbServerFile = dbServerFile;
    }

    public void run() throws IOException {
        runJar(dbServerFile);
        runJar(frontendServerFile);
        socketMessageServer.start();
    }

    private void runJar(String pathToFile) throws IOException {
        var jarFile = new File(pathToFile);
        if (!jarFile.exists()) {
            logger.error("no such file {}", pathToFile);
            throw new IllegalStateException("no such file");
        }

        logger.info("try to run {}", pathToFile);
        new ProcessBuilder("java", "-jar", jarFile.getCanonicalPath())
                .inheritIO()
                .directory(new File(jarFile.getParent()))
                .start();
    }
}
