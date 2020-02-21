package ru.otus.atm;

import ru.otus.atm.backup.AtmState;
import ru.otus.atm.cash.Cash;
import ru.otus.atm.cashissuing.CashIssuing;
import ru.otus.atm.exception.IllegalAmountException;
import ru.otus.atm.storage.CashStorage;
import ru.otus.atm.storage.Storage;

public class Atm {
    private Storage storage;

    public Atm(CashIssuing cashIssuing) {
        storage = new CashStorage(cashIssuing);
    }

    public Atm(Atm atm) throws IllegalAmountException {
        final Cash cash = atm.getCash(atm.getCashAmount());
        storage = new CashStorage(atm.getCashIssuing());
        // TODO: avoid put cash
        atm.putCash(cash);
        storage.put(cash.getBanknotes());
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

    public CashIssuing getCashIssuing() {
        return storage.getCashIssuing();
    }

    public Storage getStorage() {
        return storage;
    }

    public void load(AtmState save) {
        storage = save.getAtm().getStorage();
    }

    public AtmState save() throws IllegalAmountException {
        return new AtmState(this);
    }
}
