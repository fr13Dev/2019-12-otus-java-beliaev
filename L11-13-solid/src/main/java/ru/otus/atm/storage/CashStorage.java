package ru.otus.atm.storage;

import ru.otus.atm.cash.Banknote;
import ru.otus.atm.cash.Denominations;
import ru.otus.atm.cashissuing.CashIssuing;
import ru.otus.atm.exception.IllegalAmountException;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

public class CashStorage implements Storage {
    private List<Cell> storage = new ArrayList<>();
    private CashIssuing cashIssuing;
    private int balance;

    public CashStorage(CashIssuing cashIssuing) {
        this.cashIssuing = cashIssuing;
        initStorage();
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

    public Snapshot makeSnapshot() {
        return new Snapshot(this, storage, cashIssuing, balance);
    }

    public static class Snapshot {
        private final CashStorage cashStorage;
        private final List<Cell> storage = new ArrayList<>();
        private final List<CashCell.Snapshot> cellsSnapshot = new ArrayList<>();
        private final CashIssuing cashIssuing;
        private int balance;

        public Snapshot(CashStorage cashStorage, List<Cell> storage, CashIssuing cashIssuing, int balance) {
            this.cashStorage = cashStorage;
            for (Cell cell : storage) {
                cellsSnapshot.add(((CashCell) cell).makeSnapshot());
            }
            this.storage.addAll(storage);
            this.cashIssuing = cashIssuing;
            this.balance = balance;
        }

        public void restore() {
            for (CashCell.Snapshot snapshot : cellsSnapshot) {
                snapshot.restore();
            }
            cashStorage.setStorage(storage);
            cashStorage.setCashIssuing(cashIssuing);
            cashStorage.setBalance(balance);
        }
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

    private void setStorage(List<Cell> storage) {
        this.storage = storage;
    }

    private void setCashIssuing(CashIssuing cashIssuing) {
        this.cashIssuing = cashIssuing;
    }

    private void setBalance(int balance) {
        this.balance = balance;
    }
}

