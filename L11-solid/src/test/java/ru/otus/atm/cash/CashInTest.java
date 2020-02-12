package ru.otus.atm.cash;

import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class CashInTest {

    @Test
    public void shouldAddSomeBanknotesAndReturnAmount() {
        final CashIn cashIn = new CashIn(
                List.of(new Banknote(5_000), new Banknote(100)));
        final int amount = cashIn.getAmount();
        assertEquals(5_100, amount);
    }

    @Test
    public void shouldAddBanknoteAndReturnAmount() {
        final CashIn cashIn = new CashIn(new Banknote(1_000));
        final int amount = cashIn.getAmount();
        assertEquals(1_000, amount);
    }

}