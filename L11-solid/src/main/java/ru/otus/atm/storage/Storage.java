package ru.otus.atm.storage;

import ru.otus.atm.cash.Banknote;
import ru.otus.atm.cash.Cash;

public interface Storage {

    void add(Cash cash);

    Cell getCell(Banknote banknote);
}
