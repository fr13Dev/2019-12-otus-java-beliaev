package ru.otus.jdbc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public class DBExecutor<T> {
    private final static Logger logger = LoggerFactory.getLogger(DBExecutor.class);

    public long insertRecord(Connection connection, String sql, List<String> params) throws SQLException {
        var savepoint = connection.setSavepoint("before-inserting-record");
        try (final PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            for (int i = 0; i < params.size(); i++) {
                statement.setString(i + 1, params.get(i));
            }
            statement.executeUpdate();
            try (var rs = statement.getGeneratedKeys()) {
                rs.next();
                return rs.getInt(1);
            }

        } catch (SQLException e) {
            connection.rollback(savepoint);
            logger.error(e.getMessage(), e);
            throw e;
        }
    }

    public Optional<T> selectRecord(Connection connection, String sql, long id, Function<ResultSet, T> rsHandler) throws SQLException {
        try (var statement = connection.prepareStatement(sql)) {
            statement.setLong(1, id);
            try (var rs = statement.executeQuery()) {
                return Optional.ofNullable(rsHandler.apply(rs));
            }
        }
    }

}
