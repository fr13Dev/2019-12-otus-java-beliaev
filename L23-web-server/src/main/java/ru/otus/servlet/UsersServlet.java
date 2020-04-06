package ru.otus.servlet;

import ru.otus.dao.UserDao;
import ru.otus.service.TemplateProcessor;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class UsersServlet extends HttpServlet {
    private static final String TEMPLATE_ATTR_USERS = "users";
    private static final String TEMPLATE_ATTR_CURRENT_USER = "user";
    private static final String USERS_PAGE = "users.html";

    private final TemplateProcessor templateProcessor;
    private final UserDao userDao;

    public UsersServlet(UserDao userDao, TemplateProcessor templateProcessor) {
        this.templateProcessor = templateProcessor;
        this.userDao = userDao;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Map<String, Object> paramsMap = new HashMap<>();
        userDao.findAll().ifPresent(users -> paramsMap.put(TEMPLATE_ATTR_USERS, users));
        paramsMap.put(TEMPLATE_ATTR_CURRENT_USER, req.getSession().getAttribute(TEMPLATE_ATTR_CURRENT_USER));
        resp.setContentType("text/html");
        var page = templateProcessor.getPage(USERS_PAGE, paramsMap);
        resp.getWriter().print(page);
    }
}
