package ru.otus.atm.storage;

import ru.otus.atm.cash.Banknote;
import ru.otus.atm.exception.IllegalAmountException;

import java.util.List;

public interface Storage {

    void put(Banknote banknote);

    void put(List<Banknote> banknotes);

    List<Banknote> get(int amount) throws IllegalAmountException;

    List<Banknote> getAvailableBanknotes();

    int getAvailableQuantityOfBanknote(Banknote banknote);

    int getBalance();
}
