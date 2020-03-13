package ru.otus.jdbc.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.core.dao.Dao;
import ru.otus.core.dao.DaoException;
import ru.otus.core.model.Account;
import ru.otus.core.sessionmanager.SessionManager;
import ru.otus.core.sql.SqlGenerator;
import ru.otus.jdbc.DBExecutor;
import ru.otus.jdbc.sessionmanager.SessionManagerJdbc;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class AccountDaoJdbc implements Dao<Account> {
    private static final Logger logger = LoggerFactory.getLogger(AccountDaoJdbc.class);
    private final SessionManagerJdbc sessionManager;
    private final DBExecutor<Account> dbExecutor;
    private final SqlGenerator<Account> sqlGenerator = new SqlGenerator<>(Account.class);

    public AccountDaoJdbc(SessionManagerJdbc sessionManager, DBExecutor<Account> dbExecutor) {
        this.sessionManager = sessionManager;
        this.dbExecutor = dbExecutor;
    }

    @Override
    public Optional<Account> findById(long id) {
        try {
            return dbExecutor.selectRecord(getConnection(), sqlGenerator.getSelectQuery(), id, resultSet -> {
                try {
                    if (resultSet.next()) {
                        return new Account(resultSet.getLong(1), resultSet.getString("type"), resultSet.getInt("rest"));
                    }
                } catch (SQLException e) {
                    logger.error(e.getMessage(), e);
                }
                return null;
            });
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
        }
        return Optional.empty();
    }

    @Override
    public long save(Account account) {
        try {
            return dbExecutor.insertRecord(getConnection(), sqlGenerator.getInsertQuery(), List.of(account.getType(), String.valueOf(account.getRest())));
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            throw new DaoException(e);
        }
    }

    @Override
    public SessionManager getSessionManager() {
        return sessionManager;
    }

    private Connection getConnection() {
        return sessionManager.getCurrentSession().getConnection();
    }
}
