package ru.otus.atm;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import ru.otus.atm.cash.CashTest;
import ru.otus.atm.cashissuing.MinimumBanknotesQuantityTest;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        CashTest.class,
        MinimumBanknotesQuantityTest.class,
        ATMTest.class
})
public class AllTests {
}
