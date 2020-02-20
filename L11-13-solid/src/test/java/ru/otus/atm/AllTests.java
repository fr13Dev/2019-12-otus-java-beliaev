package ru.otus.atm;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import ru.otus.atm.cash.CashTest;
import ru.otus.atm.cashissuing.MinimumBanknotesQuantityTest;
import ru.otus.atm.storage.CashCellTest;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        CashTest.class,
        MinimumBanknotesQuantityTest.class,
        AtmTest.class,
        CashCellTest.class
})
public class AllTests {
}
