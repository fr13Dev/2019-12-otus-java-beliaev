package ru.otus.core.service;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.otus.core.dao.UserDao;
import ru.otus.core.model.User;
import ru.otus.h2.DataSourceH2;
import ru.otus.jdbc.DBExecutor;
import ru.otus.jdbc.dao.UserDaoJdbc;
import ru.otus.jdbc.sessionmanager.SessionManagerJdbc;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Optional;

import static org.junit.Assert.assertEquals;

public class DBServiceUserImplTest {
    private final DataSource dataSource = new DataSourceH2();
    private final SessionManagerJdbc sessionManager = new SessionManagerJdbc(dataSource);
    private final DBExecutor<User> dbExecutor = new DBExecutor<>();
    private final UserDao userDao = new UserDaoJdbc(sessionManager, dbExecutor);
    private final DBServiceUser dbServiceUser = new DBServiceUserImpl(userDao);
    private final User user = new User(1, "user1", 30);

    @Before
    public void createTables() throws SQLException {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement pst = connection.prepareStatement("create table user(id long(20) auto_increment NOT NULL, name varchar(255), age int(3))")) {
            pst.executeUpdate();
        }
        try (Connection connection = dataSource.getConnection();
             PreparedStatement pst = connection.prepareStatement("create table account(id long(20) auto_increment NOT NULL, type varchar(255), rest int)")) {
            pst.executeUpdate();
        }
    }

    @After
    public void dropTables() throws SQLException {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement pst = connection.prepareStatement("drop table user")) {
            pst.executeUpdate();
        }
        try (Connection connection = dataSource.getConnection();
             PreparedStatement pst = connection.prepareStatement("drop table account")) {
            pst.executeUpdate();
        }
    }

    @Test
    public void createUser() {
        final long id = dbServiceUser.save(user);
        assertEquals(1, id);
    }

    @Test
    public void findUser() {
        final long id = dbServiceUser.save(user);
        final Optional<User> optionalUser = dbServiceUser.getById(id);
        assertEquals(user, optionalUser.orElse(null));
    }
}