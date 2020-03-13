package ru.otus.core.sql;

import org.junit.Test;
import ru.otus.core.model.Account;
import ru.otus.core.model.User;

import static org.junit.Assert.assertEquals;

public class SqlGeneratorTest {
    private static final String SELECT_USER = "SELECT id,name,age FROM user WHERE id= ?";
    private static final String INSERT_USER = "INSERT INTO user(name,age) VALUES(?,?)";
    private static final String SELECT_ACCOUNT = "SELECT id,type,rest FROM account WHERE id= ?";
    private static final String INSERT_ACCOUNT = "INSERT INTO account(type,rest) VALUES(?,?)";
    private static final SqlGenerator<User> sqlGeneratorUser = new SqlGenerator<>(User.class);
    private static final SqlGenerator<Account> sqlGeneratorAccount = new SqlGenerator<>(Account.class);

    @Test
    public void getSelectUserQuery() {
        final String selectQuery = sqlGeneratorUser.getSelectQuery();
        assertEquals(SELECT_USER, selectQuery);
    }

    @Test
    public void getInsertUserQuery() {
        final String insertQuery = sqlGeneratorUser.getInsertQuery();
        assertEquals(INSERT_USER, insertQuery);
    }

    @Test
    public void getSelectAccountQuery() {
        final String selectQuery = sqlGeneratorAccount.getSelectQuery();
        assertEquals(SELECT_ACCOUNT, selectQuery);
    }

    @Test
    public void getInsertAccountQuery() {
        final String insertQuery = sqlGeneratorAccount.getInsertQuery();
        assertEquals(INSERT_ACCOUNT, insertQuery);
    }
}