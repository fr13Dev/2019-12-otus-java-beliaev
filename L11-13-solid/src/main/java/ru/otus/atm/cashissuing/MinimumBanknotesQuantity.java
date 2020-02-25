package ru.otus.atm.cashissuing;

import ru.otus.atm.cash.Banknote;
import ru.otus.atm.exception.IllegalAmountException;
import ru.otus.atm.storage.Storage;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class MinimumBanknotesQuantity implements CashIssuing {

    @Override
    public List<Banknote> getBanknotes(Storage storage, int amount) throws IllegalAmountException {
        int originalAmount = amount;
        final List<Banknote> availableBanknotesByDesc = storage.getAvailableBanknotes().collect(Collectors.toList());
        List<Banknote> banknotes = new ArrayList<>();
        for (Banknote banknote : availableBanknotesByDesc) {
            var denomination = banknote.getDenomination();
            var currentAmount = amount - (amount % denomination);
            if (currentAmount != 0) {
                var remainAmount = amount - currentAmount;
                var qntyAvailableBanknotes = storage.getAvailableQuantityOfBanknote(banknote);
                var currentAvailableAmount = qntyAvailableBanknotes * denomination;
                if (remainAmount != 0 && currentAmount <= currentAvailableAmount) {
                    if (isPossibleToGiveAmountByExistingBanknotes(availableBanknotesByDesc, remainAmount)) {
                        continue;
                    }
                }
                if (qntyAvailableBanknotes == 0) {
                    amount += currentAmount - currentAvailableAmount;
                } else if (currentAmount < currentAvailableAmount) {
                    addBanknotes(banknotes, denomination, 1);
                    amount -= denomination;
                } else {
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

    private boolean isPossibleToGiveAmountByExistingBanknotes(List<Banknote> banknotes, int amount) {
        return banknotes.stream().noneMatch(i -> i.getDenomination() == amount);
    }

    private void addBanknotes(List<Banknote> banknotesToIssue, int denomination, int qntyAvailableBanknotes) {
        for (int i = 0; i < qntyAvailableBanknotes; i++) {
            banknotesToIssue.add(new Banknote(denomination));
        }
    }
}
