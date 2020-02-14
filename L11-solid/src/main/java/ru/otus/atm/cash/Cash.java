package ru.otus.atm.cash;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    public Map<Banknote, Integer> getBanknotes() {
        final Map<Banknote, Integer> result = new HashMap<>();
        banknotes.stream().distinct().forEach(banknote -> result.put(
                banknote,
                (int) banknotes.stream().
                        filter(i -> i.equals(banknote))
                        .count()));
        return result;
    }

    public int getBanknotesQuantity() {
        return getBanknotes().values()
                .stream()
                .mapToInt(Integer::intValue)
                .sum();
    }
}
