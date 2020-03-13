package ru.otus.core.service;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.otus.core.dao.Dao;
import ru.otus.core.model.Account;
import ru.otus.h2.DataSourceH2;
import ru.otus.jdbc.DBExecutor;
import ru.otus.jdbc.dao.AccountDaoJdbc;
import ru.otus.jdbc.sessionmanager.SessionManagerJdbc;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Optional;

import static org.junit.Assert.assertEquals;

public class DBServiceAccountImplTest {
    private final DataSource dataSource = new DataSourceH2();
    private final SessionManagerJdbc sessionManager = new SessionManagerJdbc(dataSource);
    private final DBExecutor<Account> dbExecutor = new DBExecutor<>();
    private final Dao<Account> accountDao = new AccountDaoJdbc(sessionManager, dbExecutor);
    private final DBService<Account> dbServiceAccount = new DBServiceAccountImpl(accountDao);
    private final Account account = new Account(1, "account1", 30);

    @Before
    public void createTable() throws SQLException {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement pst = connection.prepareStatement("create table account(id long(20) auto_increment NOT NULL, type varchar(255), rest int)")) {
            pst.executeUpdate();
        }
    }

    @After
    public void dropTable() throws SQLException {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement pst = connection.prepareStatement("drop table account")) {
            pst.executeUpdate();
        }
    }

    @Test
    public void createAccount() {
        final long id = dbServiceAccount.save(account);
        assertEquals(1, id);
    }

    @Test
    public void findAccount() {
        final long id = dbServiceAccount.save(account);
        final Optional<Account> optionalAccount = dbServiceAccount.getById(id);
        assertEquals(account, optionalAccount.orElse(null));
    }
}