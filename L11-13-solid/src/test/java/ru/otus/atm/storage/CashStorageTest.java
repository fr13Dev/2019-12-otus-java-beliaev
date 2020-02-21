package ru.otus.atm.storage;

import org.junit.Test;
import ru.otus.atm.AbstractTest;
import ru.otus.atm.backup.Backup;
import ru.otus.atm.backup.StorageBackup;
import ru.otus.atm.backup.StorageState;
import ru.otus.atm.cashissuing.MinimumBanknotesQuantity;
import ru.otus.atm.exception.IllegalAmountException;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class CashStorageTest extends AbstractTest {
    private final CashStorage storage = new CashStorage(new MinimumBanknotesQuantity());

    @Test
    public void putSomeBanknotesGivePartOfThemBackAndRestoreState() throws IllegalAmountException {
        storage.put(List.of(ONE_THOUSAND, ONE_THOUSAND, SEVEN_HUNDRED));
        final Backup<StorageState> storageBackup = new StorageBackup();
        storageBackup.setState(storage.save());
        storage.get(2_000);
        storage.load(storageBackup.getState());
        assertEquals(2_700, storage.getBalance());
    }
}