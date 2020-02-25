package ru.otus.atm.cash;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class Cash {
    private final List<Banknote> banknotes = new ArrayList<>();

    public Cash(List<Banknote> banknotes) {
        this.banknotes.addAll(banknotes);
    }

    public Cash(Banknote banknote) {
        banknotes.add(banknote);
    }

    public int getAmount() {
        return banknotes.stream().mapToInt(Banknote::getDenomination).sum();
    }

    public Stream<Banknote> getBanknotes() {
        return banknotes.stream();
    }

    public int getBanknotesQuantity() {
        return banknotes.size();
    }
}
