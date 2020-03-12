package ru.otus;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.core.dao.UserDao;
import ru.otus.core.model.User;
import ru.otus.core.service.DBServiceUser;
import ru.otus.core.service.DBServiceUserImpl;
import ru.otus.h2.DataSourceH2;
import ru.otus.jdbc.DBExecutor;
import ru.otus.jdbc.dao.UserDaoJdbc;
import ru.otus.jdbc.sessionmanager.SessionManagerJdbc;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Optional;

public class DBServiceDemo {
    private static Logger logger = LoggerFactory.getLogger(DBServiceDemo.class);

    public static void main(String[] args) throws Exception {
        DataSource dataSource = new DataSourceH2();
        DBServiceDemo demo = new DBServiceDemo();

        demo.createTable(dataSource);

        SessionManagerJdbc sessionManager = new SessionManagerJdbc(dataSource);
        DBExecutor<User> dbExecutor = new DBExecutor<>();
        UserDao userDao = new UserDaoJdbc(sessionManager, dbExecutor);

        DBServiceUser dbServiceUser = new DBServiceUserImpl(userDao);
        long id = dbServiceUser.save(new User(0, "dbServiceUser", 4));
        Optional<User> user = dbServiceUser.getById(id);

        System.out.println(user);
        user.ifPresentOrElse(
                crUser -> logger.info("created user, name:{}", crUser.getName()),
                () -> logger.info("user was not created")
        );
    }

    private void createTable(DataSource dataSource) throws SQLException {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement pst = connection.prepareStatement("create table user(id long(20) auto_increment, name varchar(255), age int(3))")) {
            pst.executeUpdate();
        }
        System.out.println("table created");
    }
}
