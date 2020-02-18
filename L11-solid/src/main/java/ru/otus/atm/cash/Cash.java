package ru.otus.atm.cash;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Cash {
    private final List<Banknote> banknotes = new ArrayList<>();
    private int amount;

    public Cash(List<Banknote> banknotes) {
        this.banknotes.addAll(banknotes);
        amount += banknotes.stream().mapToInt(Banknote::getDenomination).sum();
    }

    public Cash(Banknote banknote) {
        banknotes.add(banknote);
        amount += banknote.getDenomination();
    }

    public int getAmount() {
        return amount;
    }

    public List<Banknote> getBanknotes() {
        return Collections.unmodifiableList(banknotes);
    }

    public int getBanknotesQuantity() {
        return banknotes.size();
    }
}
