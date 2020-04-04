package ru.otus.servlet;

import ru.otus.service.UserAuthService;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class LoginServlet extends HttpServlet {
    private static final String LOGIN_PARAMETER = "login";
    private static final String PASSWORD_PARAMETER = "password";
    private static final String CURRENT_USER_REQ_ATTR = "user";
    private static final int MAX_INACTIVE_INTERVAL = 30;
    private static final String USERS_PAGE = "/users";
    private static final String MAIN_PAGE = "/";

    private final UserAuthService authService;

    public LoginServlet(UserAuthService authService) {
        this.authService = authService;
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        var login = req.getParameter(LOGIN_PARAMETER);
        var password = req.getParameter(PASSWORD_PARAMETER);

        if (authService.authenticate(login, password)) {
            var session = req.getSession();
            session.setMaxInactiveInterval(MAX_INACTIVE_INTERVAL);
            session.setAttribute(CURRENT_USER_REQ_ATTR, login);
            resp.sendRedirect(USERS_PAGE);
        } else {
            resp.sendRedirect(MAIN_PAGE);
        }
    }
}
