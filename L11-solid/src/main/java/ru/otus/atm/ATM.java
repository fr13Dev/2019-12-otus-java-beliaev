package ru.otus.atm;

import ru.otus.atm.cash.Banknote;
import ru.otus.atm.cash.Cash;
import ru.otus.atm.cashissuing.CashIssuing;
import ru.otus.atm.exception.IllegalAmountException;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ATM {
    private final Map<Banknote, Integer> cells = new HashMap<>();
    private final CashIssuing cashIssuing;
    private int amount;

    public ATM(CashIssuing cashIssuing) {
        this.cashIssuing = cashIssuing;
    }

    public void putCash(Cash cash) {
        cash.getBanknotes().forEach((key, value) -> {
            Integer amount = cells.get(key);
            if (amount == null) {
                cells.put(key, value);
            } else {
                cells.replace(key, amount + value);
            }
        });
        amount += cash.getAmount();
    }

    public Cash getCash(int amount) throws IllegalAmountException {
        List<Banknote> banknotes = cashIssuing.getBanknotes(Collections.unmodifiableMap(cells), amount);
        final Cash cashOut = new Cash(banknotes);
        updateCellsAfterCashIssue(cashOut);
        this.amount -= cashOut.getAmount();
        return cashOut;
    }

    public int getCashAmount() {
        return amount;
    }

    private void updateCellsAfterCashIssue(Cash cashOut) {
        cashOut.getBanknotes().forEach((key, newAmount) -> {
            Integer oldAmount = cells.get(key);
            final int newValue = oldAmount - newAmount;
            if (newValue == 0) {
                cells.remove(key);
            } else {
                cells.replace(key, newValue);
            }
        });
    }
}
