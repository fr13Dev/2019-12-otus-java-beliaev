package ru.otus.core.sql;

import org.junit.Test;
import ru.otus.core.model.User;

import static org.junit.Assert.assertEquals;

public class SqlGeneratorTest {
    private final static String SELECT_USER = "select id,name,age from user where id= ?";
    private final static String INSERT_USER = "insert into user(name,age) values(?,?)";
    private final SqlGenerator<User> sqlGeneratorUser = new SqlGenerator<>(User.class);

    @Test
    public void getSelectQuery() {
        final String selectQuery = sqlGeneratorUser.getSelectQuery();
        assertEquals(SELECT_USER, selectQuery);
    }

    @Test
    public void getInsertQuery() {
        final String insertQuery = sqlGeneratorUser.getInsertQuery();
        assertEquals(INSERT_USER, insertQuery);
    }
}