package ru.otus.atm.recovering.state;

import ru.otus.atm.storage.CashStorage;

public class StorageState implements State<CashStorage> {
    private final CashStorage storage;

    public StorageState(CashStorage storage) {
        this.storage = new CashStorage(storage);
    }

    @Override
    public CashStorage get() {
        return storage;
    }
}
