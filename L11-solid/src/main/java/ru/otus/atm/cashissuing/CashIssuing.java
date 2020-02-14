package ru.otus.atm.cashissuing;

import ru.otus.atm.cash.Banknote;
import ru.otus.atm.exception.IllegalAmountException;

import java.util.List;
import java.util.Map;

@FunctionalInterface
public interface CashIssuing {

    List<Banknote> getBanknotes(Map<Banknote, Integer> atmCells, int amount) throws IllegalAmountException;
}
