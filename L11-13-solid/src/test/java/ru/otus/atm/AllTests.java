package ru.otus.atm;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import ru.otus.atm.cash.CashTest;
import ru.otus.atm.cashissuing.MinimumBanknotesQuantityTest;
import ru.otus.atm.storage.CashCellTest;
import ru.otus.department.DepartmentTest;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        CashTest.class,
        MinimumBanknotesQuantityTest.class,
        AtmTest.class,
        CashCellTest.class,
        DepartmentTest.class
})
public class AllTests {
}
