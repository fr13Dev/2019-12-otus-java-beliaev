package ru.otus.atm;

import ru.otus.atm.cash.Banknote;
import ru.otus.atm.cash.CashIn;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ATM {
    private final Map<Banknote, Integer> cells = new HashMap<>();
    private int amount;

    public void putCash(CashIn cash) {

    }

    public List<Banknote> getCash(int amount) {
        return Collections.emptyList();
    }

    public int getCashAmount() {
        return amount;
    }
}
