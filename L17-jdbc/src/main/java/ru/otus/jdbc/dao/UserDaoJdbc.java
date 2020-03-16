package ru.otus.jdbc.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.core.dao.Dao;
import ru.otus.core.dao.DaoException;
import ru.otus.core.model.User;
import ru.otus.core.sessionmanager.SessionManager;
import ru.otus.core.sql.JdbcMapper;
import ru.otus.jdbc.DBExecutor;
import ru.otus.jdbc.sessionmanager.SessionManagerJdbc;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class UserDaoJdbc implements Dao<User> {
    private static final Logger logger = LoggerFactory.getLogger(UserDaoJdbc.class);
    private final SessionManagerJdbc sessionManager;
    private final DBExecutor<User> dbExecutor;
    private final JdbcMapper jdbcMapper;

    public UserDaoJdbc(SessionManagerJdbc sessionManager, DBExecutor<User> dbExecutor, JdbcMapper jdbcMapper) {
        this.sessionManager = sessionManager;
        this.dbExecutor = dbExecutor;
        this.jdbcMapper = jdbcMapper;
    }

    @Override
    public Optional<User> findById(long id) {
        try {
            return dbExecutor.selectRecord(getConnection(), jdbcMapper.getSelectQuery(), id, resultSet -> {
                try {
                    if (resultSet.next()) {
                        return new User(resultSet.getLong("id"), resultSet.getString("name"), resultSet.getInt("age"));
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
    public long save(User user) {
        try {
            return dbExecutor.insertRecord(getConnection(), jdbcMapper.getInsertQuery(), List.of(user.getName(), String.valueOf(user.getAge())));
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
