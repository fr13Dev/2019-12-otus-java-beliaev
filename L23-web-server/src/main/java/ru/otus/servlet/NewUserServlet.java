package ru.otus.servlet;

import ru.otus.dao.UserDao;
import ru.otus.model.User;
import ru.otus.service.TemplateProcessor;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;

public class NewUserServlet extends HttpServlet {
    private static final String NAME_PARAMETER = "name";
    private static final String LOGIN_PARAMETER = "login";
    private static final String PASSWORD_PARAMETER = "password";
    private static final String USERS_PAGE = "/users";
    private static final String NEW_USER_PAGE = "new.html";


    private final TemplateProcessor templateProcessor;
    private final UserDao userDao;

    public NewUserServlet(UserDao userDao, TemplateProcessor templateProcessor) {
        this.templateProcessor = templateProcessor;
        this.userDao = userDao;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("text/html");
        var page = templateProcessor.getPage(NEW_USER_PAGE, Collections.emptyMap());
        resp.getWriter().print(page);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        var name = req.getParameter(NAME_PARAMETER);
        var login = req.getParameter(LOGIN_PARAMETER);
        var password = req.getParameter(PASSWORD_PARAMETER);

        if (name == null || login == null || password == null) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        } else if (name.isEmpty() || login.isEmpty() || password.isEmpty()) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        } else {
            userDao.insert(new User(name, login, password));
            resp.sendRedirect(USERS_PAGE);
        }
    }
}
