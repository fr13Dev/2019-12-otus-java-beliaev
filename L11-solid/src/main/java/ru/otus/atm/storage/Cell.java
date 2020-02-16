package ru.otus.atm.storage;

import ru.otus.atm.cash.Banknote;
import ru.otus.atm.exception.IllegalAmountException;

import java.util.List;

public interface Cell {

    Banknote getBaseBanknote();

    int getBanknotesQuantity();

    void addBanknotes(int quantity);

    List<Banknote> getBanknotes(int quantity) throws IllegalAmountException;
}
