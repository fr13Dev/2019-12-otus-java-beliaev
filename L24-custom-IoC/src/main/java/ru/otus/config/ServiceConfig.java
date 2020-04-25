package ru.otus.config;

import ru.otus.appcontainer.api.AppComponent;
import ru.otus.appcontainer.api.AppComponentsContainerConfig;
import ru.otus.services.IOService;
import ru.otus.services.IOServiceConsole;
import ru.otus.services.PlayerService;
import ru.otus.services.PlayerServiceImpl;

import java.util.Scanner;

@AppComponentsContainerConfig(order = 0)
public class ServiceConfig {

    @AppComponent(order = 0, name = "ioService")
    public IOService ioService() {
        return new IOServiceConsole(System.out, new Scanner(System.in));
    }

    @AppComponent(order = 1, name = "playerService")
    public PlayerService playerService(IOService ioService) {
        return new PlayerServiceImpl(ioService);
    }
}
