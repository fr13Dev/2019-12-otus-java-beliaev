package ru.otus;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import ru.otus.core.cachehw.HwCache;
import ru.otus.core.dao.UserDao;
import ru.otus.core.model.AddressDataSet;
import ru.otus.core.model.PhoneDataSet;
import ru.otus.core.model.User;
import ru.otus.core.service.DBServiceUser;
import ru.otus.core.service.DbServiceUserImpl;
import ru.otus.core.sessionmanager.SessionManager;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

public class DbServiceTest {
    private static final long USER_ID = 1L;

    private final AddressDataSet address = new AddressDataSet("address");
    private final User user = new User(0, "user", address);
    private final PhoneDataSet phone = new PhoneDataSet("777");

    private DBServiceUser dbServiceUser;

    @Mock
    private HwCache<String, User> cache;
    @Mock
    private UserDao userDao;
    @Mock
    private SessionManager sessionManager;

    @BeforeEach
    public void setUp() {
        user.addPhone(phone);
        phone.setUser(user);

        cache = mock(HwCache.class);
        userDao = mock(UserDao.class);
        sessionManager = mock(SessionManager.class);
        given(userDao.getSessionManager()).willReturn(sessionManager);
        given(userDao.saveUser(any())).willReturn(USER_ID);
        given(cache.get(String.valueOf(USER_ID))).willReturn(user);

        dbServiceUser = new DbServiceUserImpl(userDao, cache);
    }

    @DisplayName(" должен сохранить пользователя и получить его из кэша")
    @Test
    public void shouldSaveUserAndGetItFromCache() {
        dbServiceUser.saveUser(user);
        dbServiceUser.getUser(USER_ID).orElse(null);
        verify(cache, times(1)).put(String.valueOf(USER_ID), user);
        verify(cache, times(1)).get(String.valueOf(USER_ID));
        verify(userDao, times(0)).findById(USER_ID);
    }
}
