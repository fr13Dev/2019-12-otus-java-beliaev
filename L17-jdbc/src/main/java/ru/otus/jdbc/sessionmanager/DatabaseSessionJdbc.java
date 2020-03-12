package ru.otus.jdbc.sessionmanager;

import ru.otus.core.sessionmanager.DataBaseSession;

import java.sql.Connection;

public class DatabaseSessionJdbc implements DataBaseSession {
    private final Connection connection;

    public DatabaseSessionJdbc(Connection connection) {
        this.connection = connection;
    }

    public Connection getConnection() {
        return connection;
    }
}
