package ru.otus.atm.storage;

import ru.otus.atm.cash.Banknote;
import ru.otus.atm.cashissuing.CashIssuing;
import ru.otus.atm.exception.IllegalAmountException;

import java.util.List;
import java.util.stream.Stream;

public interface Storage {

    void put(Banknote banknote);

    void put(List<Banknote> banknotes);

    List<Banknote> get(int amount) throws IllegalAmountException;

    Stream<Banknote> getAvailableBanknotes();

    int getAvailableQuantityOfBanknote(Banknote banknote);

    int getBalance();

    CashIssuing getCashIssuing();
}
