package ru.otus.atm;

import org.junit.Test;
import ru.otus.atm.cash.Cash;
import ru.otus.atm.cashissuing.MinimumBanknotesQuantity;
import ru.otus.atm.exception.IllegalAmountException;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class ATMTest extends AbstractTest {
    private final ATM atm = new ATM(new MinimumBanknotesQuantity());

    @Test
    public void putOneBanknoteAndReturnCashAmount() {
        atm.putCash(new Cash(ONE_THOUSAND));
        final int actualAmount = atm.getCashAmount();
        assertEquals(ONE_THOUSAND.getDenomination(), actualAmount);
    }

    @Test
    public void putSomeBanknotesAndReturnCashAmount() {
        final Cash cashIn = new Cash(
                List.of(ONE_THOUSAND, SEVEN_HUNDRED));
        atm.putCash(cashIn);
        final int actualAmount = atm.getCashAmount();
        assertEquals(cashIn.getAmount(), actualAmount);
    }

    @Test
    public void putSomeBanknotesForOneTimeInEmptyATMAndGiveAllCashBack() throws IllegalAmountException {
        final Cash cashIn = new Cash(
                List.of(ONE_THOUSAND, SEVEN_HUNDRED, SEVEN_HUNDRED));
        atm.putCash(cashIn);
        final Cash cashOut = atm.getCash(atm.getCashAmount());
        assertEquals(cashIn.getAmount(), cashOut.getAmount());
        assertEquals(0, atm.getCashAmount());
        assertEquals(3, cashOut.getBanknotesQuantity());
    }

    @Test
    public void putSomeBanknotesForManyTimesInEmptyATMAndGiveAllCashBack() throws IllegalAmountException {
        final Cash cashIn = new Cash(
                List.of(ONE_THOUSAND, SEVEN_HUNDRED));
        atm.putCash(cashIn);
        atm.putCash(new Cash(SEVEN_HUNDRED));
        final int amountInAtm = atm.getCashAmount();
        final Cash cashOut = atm.getCash(amountInAtm);
        assertEquals(amountInAtm, cashOut.getAmount());
        assertEquals(0, atm.getCashAmount());
        assertEquals(3, cashOut.getBanknotesQuantity());
    }

    @Test
    public void putSomeBanknotesForManyTimesAndGiveOneOfThemBack() throws IllegalAmountException {
        final Cash cashIn = new Cash(
                List.of(ONE_THOUSAND, SEVEN_HUNDRED));
        atm.putCash(cashIn);
        atm.putCash(new Cash(SEVEN_HUNDRED));
        final int amountInAtm = atm.getCashAmount();
        final Cash cashOut = atm.getCash(SEVEN_HUNDRED.getDenomination());
        assertEquals(SEVEN_HUNDRED.getDenomination(), cashOut.getAmount());
        assertEquals(amountInAtm - cashOut.getAmount(), atm.getCashAmount());
        assertEquals(1, cashOut.getBanknotes().size());
        assertEquals(1, cashOut.getBanknotesQuantity());
    }

    @Test
    public void putSomeBanknotesForManyTimesAndGivePartOfThemBack() throws IllegalAmountException {
        final Cash cashIn = new Cash(
                List.of(ONE_THOUSAND, SEVEN_HUNDRED));
        atm.putCash(cashIn);
        atm.putCash(new Cash(SEVEN_HUNDRED));
        final int amountInAtm = atm.getCashAmount();
        final Cash cashOut = atm.getCash(SEVEN_HUNDRED.getDenomination() * 2);
        assertEquals(SEVEN_HUNDRED.getDenomination() * 2, cashOut.getAmount());
        assertEquals(amountInAtm - cashOut.getAmount(), atm.getCashAmount());
        assertEquals(2, cashOut.getBanknotesQuantity());
    }

    @Test
    public void putSomeBanknotesForManyTimesGivePartOfThemBackPutOneBanknoteAndGiveItBack() throws IllegalAmountException {
        final Cash cashIn = new Cash(
                List.of(ONE_THOUSAND, SEVEN_HUNDRED));
        atm.putCash(cashIn);
        atm.putCash(new Cash(SEVEN_HUNDRED));
        atm.getCash(SEVEN_HUNDRED.getDenomination() + ONE_THOUSAND.getDenomination());
        atm.putCash(new Cash(SEVEN_HUNDRED));
        final Cash cashOut = atm.getCash(SEVEN_HUNDRED.getDenomination());
        final int amountInAtm = atm.getCashAmount();
        assertEquals(SEVEN_HUNDRED.getDenomination(), amountInAtm);
        assertEquals(SEVEN_HUNDRED.getDenomination(), cashOut.getAmount());
        assertEquals(1, cashOut.getBanknotesQuantity());
    }

    @Test(expected = IllegalAmountException.class)
    public void putSomeBanknotesForOneTimeAndGiveLargerAmountBack() throws IllegalAmountException {
        final Cash cashIn = new Cash(
                List.of(ONE_THOUSAND, SEVEN_HUNDRED));
        atm.putCash(cashIn);
        atm.getCash(ONE_THOUSAND.getDenomination() * 2);
    }

    @Test(expected = IllegalAmountException.class)
    public void putSomeBanknotesForOneTimeAndGiveInvalidAmountBack() throws IllegalAmountException {
        final Cash cashIn = new Cash(
                List.of(ONE_THOUSAND, SEVEN_HUNDRED));
        atm.putCash(cashIn);
        atm.getCash(SEVEN_HUNDRED.getDenomination() * 2);
    }
}