package ru.otus.atm.cash;

import java.util.ArrayList;
import java.util.List;

class AbstractCash {
    private final List<Banknote> banknotes = new ArrayList<>();
    private int amount;

    protected AbstractCash(List<Banknote> banknotes) {
        this.banknotes.addAll(banknotes);
        amount += banknotes.stream().mapToInt(Banknote::getDenomination).sum();
    }

    protected AbstractCash(Banknote banknote) {
        banknotes.add(banknote);
        amount += banknote.getDenomination();
    }

    protected int getAmount() {
        return amount;
    }
}
