package ru.otus.core.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.core.dao.Dao;
import ru.otus.core.model.Account;
import ru.otus.core.sessionmanager.SessionManager;

import java.util.Optional;

public class DBServiceAccountImpl implements DBService<Account> {
    private static final Logger logger = LoggerFactory.getLogger(DBServiceAccountImpl.class);
    private final Dao<Account> accountDao;

    public DBServiceAccountImpl(Dao<Account> accountDao) {
        this.accountDao = accountDao;
    }

    @Override
    public long save(Account account) {
        try (SessionManager sessionManager = accountDao.getSessionManager()) {
            sessionManager.beginSession();
            try {
                var id = accountDao.save(account);
                sessionManager.commitSession();
                logger.info("account saved {}", account);
                return id;
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
                sessionManager.rollbackSession();
                throw new DBServiceException(e);
            }
        }
    }

    @Override
    public Optional<Account> getById(long id) {
        try (SessionManager sessionManager = accountDao.getSessionManager()) {
            sessionManager.beginSession();
            try {
                var optionalAccount = accountDao.findById(id);
                sessionManager.commitSession();
                logger.info("account: {}", optionalAccount.orElse(null));
                return optionalAccount;
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
                sessionManager.rollbackSession();
                throw new DBServiceException(e);
            }
        }
    }
}
