package ru.otus.atm;

import ru.otus.atm.cash.Cash;
import ru.otus.atm.cashissuing.CashIssuing;
import ru.otus.atm.exception.IllegalAmountException;
import ru.otus.atm.storage.CashStorage;
import ru.otus.atm.storage.Storage;

public class ATM {
    private final Storage storage;

    public ATM(CashIssuing cashIssuing) {
        storage = new CashStorage(cashIssuing);
    }

    public void putCash(Cash cash) {
        storage.put(cash.getBanknotes());
    }

    public Cash getCash(int amount) throws IllegalAmountException {
        return new Cash(storage.get(amount));
    }

    public int getCashAmount() {
        return storage.getBalance();
    }
}
