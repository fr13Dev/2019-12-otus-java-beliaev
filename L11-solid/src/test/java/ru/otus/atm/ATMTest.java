package ru.otus.atm;

import org.junit.Test;
import ru.otus.atm.cash.Banknote;
import ru.otus.atm.cash.CashIn;

import static org.junit.Assert.*;

public class ATMTest {
    private final ATM atm = new ATM();

    @Test
    public void shouldPutOneBanknoteAndReturnCashAmount() {
        final Banknote banknote = new Banknote(100);
        atm.putCash(new CashIn(banknote));
        final int actualAmount = atm.getCashAmount();
        assertEquals(banknote.getDenomination(), actualAmount);
    }
}