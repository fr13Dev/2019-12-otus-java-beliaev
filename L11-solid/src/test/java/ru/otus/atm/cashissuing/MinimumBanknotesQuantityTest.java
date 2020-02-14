package ru.otus.atm.cashissuing;

import org.junit.Before;
import org.junit.Test;
import ru.otus.atm.AbstractTest;
import ru.otus.atm.cash.Banknote;
import ru.otus.atm.exception.IllegalAmountException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class MinimumBanknotesQuantityTest extends AbstractTest {
    private final Map<Banknote, Integer> atmCells = new HashMap<>();
    private final CashIssuing cashIssuing = new MinimumBanknotesQuantity();

    @Before
    public void init() {
        atmCells.put(ONE_THOUSAND, 1);
        atmCells.put(SEVEN_HUNDRED, 2);
    }

    @Test
    public void getOneBanknote() throws IllegalAmountException {
        final List<Banknote> banknotes = cashIssuing.getBanknotes(atmCells, SEVEN_HUNDRED.getDenomination());
        assertEquals(1, banknotes.size());
    }

    @Test
    public void getTwoBanknotes() throws IllegalAmountException {
        final List<Banknote> banknotes = cashIssuing.getBanknotes(atmCells, SEVEN_HUNDRED.getDenomination() * 2);
        assertEquals(2, banknotes.size());
    }

    @Test
    public void getThreeBanknotes() throws IllegalAmountException {
        final List<Banknote> banknotes = cashIssuing.getBanknotes(atmCells, ONE_THOUSAND.getDenomination() + SEVEN_HUNDRED.getDenomination() * 2);
        assertEquals(3, banknotes.size());
    }

    @Test(expected = IllegalAmountException.class)
    public void getInvalidAmount() throws IllegalAmountException {
        final List<Banknote> banknotes = cashIssuing.getBanknotes(atmCells, ONE_THOUSAND.getDenomination() * 2);
        assertEquals(3, banknotes.size());
    }

    @Test(expected = IllegalAmountException.class)
    public void getAmountLargerThenPossible() throws IllegalAmountException {
        final List<Banknote> banknotes = cashIssuing.getBanknotes(atmCells, ONE_THOUSAND.getDenomination() * 3);
        assertEquals(3, banknotes.size());
    }
}