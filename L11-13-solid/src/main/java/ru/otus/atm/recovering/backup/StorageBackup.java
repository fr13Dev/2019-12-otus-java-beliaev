package ru.otus.atm.recovering.backup;

import ru.otus.atm.recovering.state.StorageState;

public class StorageBackup implements Backup<StorageState> {
    private StorageState storageState;

    @Override
    public StorageState getState() {
        return storageState;
    }

    @Override
    public void setState(StorageState storageState) {
        this.storageState = storageState;
    }
}
