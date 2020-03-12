package ru.otus.jdbc.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.core.dao.UserDao;
import ru.otus.core.dao.UserDaoException;
import ru.otus.core.model.User;
import ru.otus.core.reflection.SqlGenerator;
import ru.otus.core.sessionmanager.SessionManager;
import ru.otus.jdbc.DBExecutor;
import ru.otus.jdbc.sessionmanager.SessionManagerJdbc;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class UserDaoJdbc implements UserDao {
    private final static Logger logger = LoggerFactory.getLogger(UserDaoJdbc.class);
    private final SessionManagerJdbc sessionManager;
    private final DBExecutor<User> dbExecutor;
    private final SqlGenerator<User> sqlGenerator = new SqlGenerator<>(User.class);

    public UserDaoJdbc(SessionManagerJdbc sessionManager, DBExecutor<User> dbExecutor) {
        this.sessionManager = sessionManager;
        this.dbExecutor = dbExecutor;
    }


    @Override
    public Optional<User> findById(long id) {
        try {
            return dbExecutor.selectRecord(getConnection(), sqlGenerator.getSelectQuery(), id, resultSet -> {
                try {
                    if (resultSet.next()) {
                        return new User(resultSet.getLong(1), resultSet.getString("name"), resultSet.getInt("age"));
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
            return dbExecutor.insertRecord(getConnection(), sqlGenerator.getInsertQuery(), List.of(user.getName(), String.valueOf(user.getAge())));
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new UserDaoException(e);
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
