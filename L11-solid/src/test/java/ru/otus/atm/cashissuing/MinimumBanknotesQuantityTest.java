package ru.otus.atm.cashissuing;

import org.junit.Before;
import org.junit.Test;
import ru.otus.atm.AbstractTest;
import ru.otus.atm.cash.Banknote;
import ru.otus.atm.exception.IllegalAmountException;
import ru.otus.atm.storage.CashStorage;
import ru.otus.atm.storage.Storage;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class MinimumBanknotesQuantityTest extends AbstractTest {
    CashIssuing cashIssuing = new MinimumBanknotesQuantity();
    private final Storage storage = new CashStorage(cashIssuing);

    @Before
    public void init() {
        storage.put(ONE_THOUSAND);
        storage.put(SEVEN_HUNDRED);
        storage.put(SEVEN_HUNDRED);
    }

    @Test
    public void getOneBanknote() throws IllegalAmountException {
        final List<Banknote> banknotes = cashIssuing.getBanknotes(storage, SEVEN_HUNDRED.getDenomination());
        assertEquals(1, banknotes.size());
    }

    @Test
    public void getTwoBanknotes() throws IllegalAmountException {
        final List<Banknote> banknotes = cashIssuing.getBanknotes(storage, SEVEN_HUNDRED.getDenomination() * 2);
        assertEquals(2, banknotes.size());
    }

    @Test
    public void getThreeBanknotes() throws IllegalAmountException {
        final List<Banknote> banknotes = cashIssuing.getBanknotes(storage, ONE_THOUSAND.getDenomination() + SEVEN_HUNDRED.getDenomination() * 2);
        assertEquals(3, banknotes.size());
    }

    @Test(expected = IllegalAmountException.class)
    public void getInvalidAmount() throws IllegalAmountException {
        final List<Banknote> banknotes = cashIssuing.getBanknotes(storage, ONE_THOUSAND.getDenomination() * 2);
        assertEquals(3, banknotes.size());
    }

    @Test(expected = IllegalAmountException.class)
    public void getAmountLargerThenPossible() throws IllegalAmountException {
        final List<Banknote> banknotes = cashIssuing.getBanknotes(storage, ONE_THOUSAND.getDenomination() * 3);
        assertEquals(3, banknotes.size());
    }
}