package ru.otus.atm.backup;

// Caretaker
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
