package ru.otus.core;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import ru.otus.core.service.DBServiceAccountImplTest;
import ru.otus.core.service.DBServiceUserImplTest;
import ru.otus.core.sql.SqlGeneratorTest;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        DBServiceUserImplTest.class,
        DBServiceAccountImplTest.class,
        SqlGeneratorTest.class
})
public class AllTest {
}
