package ru.otus.atm.backup;

import ru.otus.atm.storage.CashStorage;

// Memento
public class StorageState {
    private final CashStorage storage;

    public StorageState(CashStorage storage) {
        this.storage = new CashStorage(storage);
    }

    public CashStorage getStorage() {
        return storage;
    }
}
