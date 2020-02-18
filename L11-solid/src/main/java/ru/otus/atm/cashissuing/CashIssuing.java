package ru.otus.atm.cashissuing;

import ru.otus.atm.cash.Banknote;
import ru.otus.atm.exception.IllegalAmountException;
import ru.otus.atm.storage.Storage;

import java.util.List;

@FunctionalInterface
public interface CashIssuing {

    List<Banknote> getBanknotes(Storage storage, int amount) throws IllegalAmountException;
}
