package ru.otus.atm.storage;

import ru.otus.atm.cash.Banknote;
import ru.otus.atm.cash.Cash;

import java.util.HashSet;
import java.util.Set;

public class CashStorage implements Storage {
    private final Set<Cell> storage = new HashSet<>();

    @Override
    public void add(Cash cash) {
        cash.getBanknotes().forEach((banknote, quantity) -> {

        });
    }

    @Override
    public Cell getCell(Banknote banknote) {
        return storage.stream().filter(i->i.equals(banknote)).findFirst().orElse(null);
    }
}
