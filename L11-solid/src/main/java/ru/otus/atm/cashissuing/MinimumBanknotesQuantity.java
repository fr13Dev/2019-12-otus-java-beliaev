package ru.otus.atm.cashissuing;

import ru.otus.atm.cash.Banknote;
import ru.otus.atm.exception.IllegalAmountException;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class MinimumBanknotesQuantity implements CashIssuing {

    @Override
    public List<Banknote> getBanknotes(Map<Banknote, Integer> atmCells, int amount) throws IllegalAmountException {
        int originalAmount = amount;
        final List<Banknote> availableBanknotesByDesc = atmCells.keySet().stream()
                .sorted(Comparator.comparingInt(Banknote::getDenomination).reversed())
                .collect(Collectors.toList());
        List<Banknote> banknotes = new ArrayList<>();
        for (Banknote banknote : availableBanknotesByDesc) {
            final int denomination = banknote.getDenomination();
            final int currentAmount = amount - (amount % denomination);
            if (currentAmount != 0) {
                final int remainAmount = amount - currentAmount;
                final int qntyAvailableBanknotes = atmCells.get(banknote);
                final int currentAvailableAmount = qntyAvailableBanknotes * denomination;
                if (remainAmount != 0 && currentAmount <= currentAvailableAmount) {
                    if (isPossibleToGiveAmountByExistingBanknotes(atmCells, remainAmount)) {
                        continue;
                    }
                }
                if (qntyAvailableBanknotes == 0) {
                    amount += currentAmount - currentAvailableAmount;
                } else if (currentAmount < currentAvailableAmount) {
                    addBanknotes(banknotes, denomination, 1);
                    amount -= denomination;
                }
                else {
                    addBanknotes(banknotes, denomination, qntyAvailableBanknotes);
                    amount -= qntyAvailableBanknotes * denomination;
                }
            }
        }
        if (amount != 0) {
            throw new IllegalAmountException(String.format("It is impossible to issue %d", originalAmount));
        }
        return banknotes;
    }

    private boolean isPossibleToGiveAmountByExistingBanknotes(Map<Banknote, Integer> atmCells, int amount) {
        return atmCells.keySet()
                .stream()
                .noneMatch(i -> i.getDenomination() == amount);
    }

    private void addBanknotes(List<Banknote> banknotesToIssue, int denomination, int qntyAvailableBanknotes) {
        for (int i = 0; i < qntyAvailableBanknotes; i++) {
            banknotesToIssue.add(new Banknote(denomination));
        }
    }
}
