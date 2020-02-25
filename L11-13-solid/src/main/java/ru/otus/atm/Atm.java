package ru.otus.atm;

import ru.otus.atm.cash.Cash;
import ru.otus.atm.cashissuing.CashIssuing;
import ru.otus.atm.exception.IllegalAmountException;
import ru.otus.atm.recovering.Recovering;
import ru.otus.atm.recovering.backup.Backup;
import ru.otus.atm.recovering.backup.StorageBackup;
import ru.otus.atm.recovering.state.AtmState;
import ru.otus.atm.recovering.state.StorageState;
import ru.otus.atm.storage.CashStorage;
import ru.otus.atm.storage.Storage;

import java.util.stream.Collectors;

public class Atm implements Recovering<AtmState> {
    private final Storage storage;

    public Atm(CashIssuing cashIssuing) {
        storage = new CashStorage(cashIssuing);
    }

    public Atm(Atm atm) {
        final Backup<StorageState> backup = new StorageBackup();
        backup.setState(new StorageState((CashStorage) atm.getStorage()));
        storage = new CashStorage(atm.getStorage().getCashIssuing());
        ((CashStorage) storage).load(backup.getState());
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

    @Override
    public void load(AtmState state) {
        final Backup<StorageState> backup = new StorageBackup();
        final Storage tempStorage = state.get().getStorage();
        backup.setState(new StorageState((CashStorage) tempStorage));
        ((CashStorage) storage).load(backup.getState());
    }

    @Override
    public AtmState save() {
        return new AtmState(this);
    }

    private Storage getStorage() {
        return storage;
    }
}
