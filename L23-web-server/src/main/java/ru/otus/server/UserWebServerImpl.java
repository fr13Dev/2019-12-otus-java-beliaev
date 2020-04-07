package ru.otus.server;

import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.FilterHolder;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import ru.otus.dao.UserDao;
import ru.otus.helper.FileSystemHelper;
import ru.otus.service.TemplateProcessor;
import ru.otus.service.UserAuthService;
import ru.otus.servlet.AuthorizationFilter;
import ru.otus.servlet.LoginServlet;
import ru.otus.servlet.NewUserServlet;
import ru.otus.servlet.UsersServlet;

import java.util.Arrays;

public class UserWebServerImpl implements UserWebServer {
    private static final String MAIN_PAGE = "index.html";
    private static final String COMMON_RESOURCES_DIR = "static";
    private static final String USERS_PAGE = "/users";
    private static final String NEW_USER_PAGE = "/new";
    private static final String LOGIN_PAGE = "/login";

    private final UserDao userDao;
    private final Server server;
    private final UserAuthService authService;
    private final TemplateProcessor templateProcessor;

    public UserWebServerImpl(UserDao userDao, UserAuthService authService,
                             TemplateProcessor templateProcessor, int port) {
        this.userDao = userDao;
        this.authService = authService;
        this.templateProcessor = templateProcessor;
        this.server = new Server(port);
    }

    @Override
    public void start() throws Exception {
        if (server.getHandlers().length == 0) {
            initContext();
        }
        server.start();
    }

    @Override
    public void join() throws Exception {
        server.join();
    }

    private void initContext() {
        var resourceHandler = createResourceHandler();
        var servletHandler = createServletContextHandler();
        var securityHandler = applySecurity(servletHandler, USERS_PAGE, NEW_USER_PAGE);
        var handlerList = new HandlerList(resourceHandler, securityHandler);
        server.setHandler(handlerList);
    }

    private ResourceHandler createResourceHandler() {
        var handler = new ResourceHandler();
        handler.setDirectoriesListed(false);
        handler.setWelcomeFiles(new String[]{MAIN_PAGE});
        handler.setResourceBase(
                FileSystemHelper.localFileNameOrResourceNameToFullPath(COMMON_RESOURCES_DIR));
        return handler;
    }

    private ServletContextHandler createServletContextHandler() {
        var handler = new ServletContextHandler(ServletContextHandler.SESSIONS);
        handler.addServlet(new ServletHolder(new UsersServlet(userDao, templateProcessor)), USERS_PAGE);
        handler.addServlet(new ServletHolder(new NewUserServlet(userDao, templateProcessor)), NEW_USER_PAGE);
        return handler;
    }

    private Handler applySecurity(ServletContextHandler handler, String... paths) {
        handler.addServlet(new ServletHolder(new LoginServlet(authService)), LOGIN_PAGE);
        var authorizationFilter = new AuthorizationFilter();
        Arrays.stream(paths)
                .forEachOrdered(path -> handler.addFilter(new FilterHolder(authorizationFilter), path, null));
        return handler;
    }
}
