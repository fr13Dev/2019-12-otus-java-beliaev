package ru.otus.atm.storage;

import ru.otus.atm.backup.Backup;
import ru.otus.atm.backup.CellBackup;
import ru.otus.atm.backup.CellState;
import ru.otus.atm.backup.StorageState;
import ru.otus.atm.cash.Banknote;
import ru.otus.atm.cash.Denominations;
import ru.otus.atm.cashissuing.CashIssuing;
import ru.otus.atm.exception.IllegalAmountException;

import java.util.*;
import java.util.stream.Collectors;

public class CashStorage implements Storage {
    private final Set<Cell> storage = new HashSet<>();
    private final CashIssuing cashIssuing;
    private int balance;

    public CashStorage(CashIssuing cashIssuing) {
        this.cashIssuing = cashIssuing;
        initStorage();
    }

    public CashStorage(CashStorage storage) {
        this.cashIssuing = storage.getCashIssuing();
        this.balance = storage.balance;
        storage.getStorage().forEach(i -> {
            final Backup<CellState> cellBackup = new CellBackup();
            cellBackup.setState(new CellState((CashCell) i));
            this.storage.add(cellBackup.getState().getCell());
        });
    }

    @Override
    public void put(Banknote banknote) {
        getCell(banknote).addBanknote();
        balance += banknote.getDenomination();
    }

    @Override
    public void put(List<Banknote> banknotes) {
        banknotes.forEach(this::put);
    }

    @Override
    public List<Banknote> get(int amount) throws IllegalAmountException {
        var banknotes = cashIssuing.getBanknotes(this, amount);
        banknotes.forEach(banknote -> getCell(banknote).decrementBanknoteQuantity());
        balance -= banknotes.stream().mapToInt(Banknote::getDenomination).sum();
        return banknotes;
    }

    @Override
    public List<Banknote> getAvailableBanknotes() {
        return storage.stream()
                .sorted(Comparator.comparing(Cell::getBaseBanknote, Comparator.comparingInt(Banknote::getDenomination).reversed()))
                .map(Cell::getBaseBanknote)
                .collect(Collectors.toList());
    }

    @Override
    public int getAvailableQuantityOfBanknote(Banknote banknote) {
        return getCell(banknote).getBanknotesQuantity();
    }

    @Override
    public int getBalance() {
        return balance;
    }

    public Set<Cell> getStorage() {
        return Collections.unmodifiableSet(storage);
    }

    public CashIssuing getCashIssuing() {
        return cashIssuing;
    }

    public void load(StorageState save) {
        this.balance = save.getStorage().getBalance();
        this.storage.clear();
        this.storage.addAll(save.getStorage().getStorage());
    }

    public StorageState save() {
        return new StorageState(this);
    }

    private void initStorage() {
        for (Denominations denomination : Denominations.values()) {
            storage.add(new CashCell(new Banknote(denomination)));
        }
    }

    private Cell getCell(Banknote banknote) {
        return storage.stream()
                .filter(i -> i.getBaseBanknote().equals(banknote))
                .findFirst()
                .get();
    }
}

