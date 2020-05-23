package ru.otus;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.otus.socket.SocketMessageServer;

import java.io.File;
import java.io.IOException;

@Component
public class AppRunner {
    private static final Logger logger = LoggerFactory.getLogger(AppRunner.class);

    @Autowired
    private SocketMessageServer socketMessageServer;
    @Value("${frontend.server.file}")
    private String frontendServerFile;
    @Value("${db.server.file}")
    private String dbServerFile;

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
