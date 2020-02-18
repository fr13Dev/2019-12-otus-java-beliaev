package ru.otus.atm.storage;

import ru.otus.atm.cash.Banknote;
import ru.otus.atm.cash.Denominations;
import ru.otus.atm.cashissuing.CashIssuing;
import ru.otus.atm.exception.IllegalAmountException;

import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class CashStorage implements Storage {
    private final Set<Cell> storage = new HashSet<>();
    private final CashIssuing cashIssuing;
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
        banknotes.forEach(banknote-> getCell(banknote).decrementBanknoteQuantity());
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

