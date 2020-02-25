package ru.otus.atm.storage;

import ru.otus.atm.cash.Banknote;
import ru.otus.atm.cash.Denominations;
import ru.otus.atm.cashissuing.CashIssuing;
import ru.otus.atm.exception.IllegalAmountException;
import ru.otus.atm.recovering.Recovering;
import ru.otus.atm.recovering.backup.Backup;
import ru.otus.atm.recovering.backup.CellBackup;
import ru.otus.atm.recovering.state.CellState;
import ru.otus.atm.recovering.state.StorageState;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

public class CashStorage implements Storage, Recovering<StorageState> {
    private final List<Cell> storage = new ArrayList<>();
    private final CashIssuing cashIssuing;
    private int balance;

    public CashStorage(CashIssuing cashIssuing) {
        this.cashIssuing = cashIssuing;
        initStorage();
    }

    public CashStorage(CashStorage storage) {
        this.cashIssuing = storage.getCashIssuing();
        this.balance = storage.getBalance();
        storage.getStorage().forEach(i -> {
            final Backup<CellState> cellBackup = new CellBackup();
            cellBackup.setState(new CellState((CashCell) i));
            this.storage.add(cellBackup.getState().get());
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
    public Stream<Banknote> getAvailableBanknotes() {
        return storage.stream()
                .sorted(Comparator.comparing(Cell::getBaseBanknote, Comparator.comparingInt(Banknote::getDenomination).reversed()))
                .map(Cell::getBaseBanknote);
    }

    @Override
    public int getAvailableQuantityOfBanknote(Banknote banknote) {
        return getCell(banknote).getBanknotesQuantity();
    }

    @Override
    public int getBalance() {
        return balance;
    }

    @Override
    public CashIssuing getCashIssuing() {
        return cashIssuing;
    }

    @Override
    public void load(StorageState state) {
        this.balance = state.get().getBalance();
        this.storage.clear();
        this.storage.addAll(state.get().getStorage());
    }

    @Override
    public StorageState save() {
        return new StorageState(this);
    }

    private List<Cell> getStorage() {
        return Collections.unmodifiableList(storage);
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

