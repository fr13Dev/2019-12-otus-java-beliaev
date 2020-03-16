package ru.otus.core.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.core.dao.Dao;
import ru.otus.core.model.User;
import ru.otus.core.sessionmanager.SessionManager;

import java.util.Optional;

public class DBServiceUserImpl implements DBService<User> {
    private static final Logger logger = LoggerFactory.getLogger(DBServiceUserImpl.class);
    private final Dao<User> userDao;

    public DBServiceUserImpl(Dao<User> userDao) {
        this.userDao = userDao;
    }

    @Override
    public long save(User user) {
        try (SessionManager sessionManager = userDao.getSessionManager()) {
            sessionManager.beginSession();
            try {
                var id = userDao.save(user);
                sessionManager.commitSession();
                logger.info("user saved {}", user);
                return id;
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
                sessionManager.rollbackSession();
                throw new DBServiceException(e);
            }
        }
    }

    @Override
    public Optional<User> getById(long id) {
        try (SessionManager sessionManager = userDao.getSessionManager()) {
            sessionManager.beginSession();
            try {
                var optionalUser = userDao.findById(id);
                sessionManager.commitSession();
                logger.info("user: {}", optionalUser.orElse(null));
                return optionalUser;
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
                sessionManager.rollbackSession();
                throw new DBServiceException(e);
            }
        }
    }
}
