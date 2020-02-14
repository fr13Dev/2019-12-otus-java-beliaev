package ru.otus.atm;

import ru.otus.atm.cash.Banknote;

public abstract class AbstractTest {
    protected final static Banknote ONE_THOUSAND = new Banknote(1_000);
    protected final static Banknote SEVEN_HUNDRED = new Banknote(700);
}
