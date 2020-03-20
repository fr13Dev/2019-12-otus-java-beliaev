import org.hibernate.SessionFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.otus.core.dao.UserDao;
import ru.otus.core.model.AddressDataSet;
import ru.otus.core.model.PhoneDataSet;
import ru.otus.core.model.User;
import ru.otus.core.service.DBServiceUser;
import ru.otus.core.service.DbServiceUserImpl;
import ru.otus.hibernate.HibernateUtils;
import ru.otus.hibernate.dao.UserDaoHibernate;
import ru.otus.hibernate.sessionmanager.SessionManagerHibernate;

import static org.assertj.core.api.Assertions.assertThat;

public class HibernateTest {
    private static final String HIBERNATE_CFG_XML_FILE_RESOURCE = "hibernate-test.cfg.xml";
    private static final String ID = "id";
    private static final String NAME = "name";
    private static final String ADDRESS = "address";
    private static final String NUMBER = "number";

    private final AddressDataSet address = new AddressDataSet("address");
    private final User user = new User(0, "user", address);
    private final PhoneDataSet phone = new PhoneDataSet("777");

    private SessionFactory sessionFactory;
    private DBServiceUser dbServiceUser;

    @BeforeEach
    public void setUp() {
        user.addPhone(phone);
        phone.setUser(user);

        sessionFactory = HibernateUtils.buildSessionFactory(HIBERNATE_CFG_XML_FILE_RESOURCE,
                User.class, AddressDataSet.class, PhoneDataSet.class);
        SessionManagerHibernate sessionManager = new SessionManagerHibernate(sessionFactory);
        UserDao userDao = new UserDaoHibernate(sessionManager);
        dbServiceUser = new DbServiceUserImpl(userDao);
    }

    @AfterEach
    void tearDown() {
        sessionFactory.close();
    }

    @DisplayName(" корректно сохранять пользователя")
    @Test
    public void saveUser() {
        var id = dbServiceUser.saveUser(user);
        var loadedUser = loadUser(id);
        assertThat(loadedUser).isNotNull().isEqualToComparingOnlyGivenFields(user, ID, NAME, ADDRESS);
        loadedUser.getPhones().forEach(i -> {
            var index = user.getPhones().indexOf(i);
            assertThat(i).isEqualToComparingOnlyGivenFields(user.getPhones().get(index), ID, NUMBER);
        });
    }

    private User loadUser(long id) {
        return dbServiceUser.getUser(id).orElse(null);
    }

}
