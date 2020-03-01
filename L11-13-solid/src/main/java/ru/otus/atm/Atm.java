package ru.otus.atm;

import ru.otus.atm.cash.Cash;
import ru.otus.atm.cashissuing.CashIssuing;
import ru.otus.atm.command.Command;
import ru.otus.atm.exception.IllegalAmountException;
import ru.otus.atm.storage.CashStorage;
import ru.otus.atm.storage.Storage;

import java.util.stream.Collectors;

public class Atm {
    private Storage storage;

    public Atm(CashIssuing cashIssuing) {
        storage = new CashStorage(cashIssuing);
    }

    public void putCash(Cash cash) {
        storage.put(cash.getBanknotes().collect(Collectors.toList()));
    }

    public Cash getCash(int amount) throws IllegalAmountException {
        return new Cash(storage.get(amount));
    }

    public int getCashAmount() {
        return storage.getBalance();
    }

    public Snapshot makeSnapshot() {
        return new Snapshot(this, storage);
    }

    public <T> T executeCommand(Command<T> command) {
        return command.execute(this);
    }

    public static class Snapshot {
        private final Atm atm;
        private final Storage storage;
        private final CashStorage.Snapshot storageSnapshot;

        public Snapshot(Atm atm, Storage storage) {
            this.atm = atm;
            storageSnapshot = ((CashStorage) storage).makeSnapshot();
            this.storage = storage;
        }

        public void restore() {
            storageSnapshot.restore();
            atm.setStorage(storage);
        }
    }

    private void setStorage(Storage storage) {
        this.storage = storage;
    }
}
