package ru.otus.atm.cash;

import org.junit.Test;
import ru.otus.atm.AbstractTest;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class CashTest extends AbstractTest {

    @Test
    public void addSomeBanknotesAndReturnAmount() {
        final Cash cashIn = new Cash(
                List.of(ONE_THOUSAND, SEVEN_HUNDRED));
        final int amount = cashIn.getAmount();
        assertEquals(ONE_THOUSAND.getDenomination() + SEVEN_HUNDRED.getDenomination(), amount);
    }

    @Test
    public void addOneBanknoteAndReturnAmount() {
        final Cash cashIn = new Cash(ONE_THOUSAND);
        final int amount = cashIn.getAmount();
        assertEquals(ONE_THOUSAND.getDenomination(), amount);
    }

    @Test
    public void addSomeBanknotesAndReturnBanknotes() {
        var cashIn = new Cash(
                List.of(ONE_THOUSAND, ONE_THOUSAND, SEVEN_HUNDRED));
        assertEquals(3, cashIn.getBanknotesQuantity());
        assertEquals(2_700, cashIn.getAmount());
    }

    @Test
    public void addSomeBanknotesAndReturnBanknotesQuantity() {
        final Cash cashIn = new Cash(
                List.of(ONE_THOUSAND, ONE_THOUSAND, SEVEN_HUNDRED, SEVEN_HUNDRED));
        assertEquals(4, cashIn.getBanknotesQuantity());
    }
}