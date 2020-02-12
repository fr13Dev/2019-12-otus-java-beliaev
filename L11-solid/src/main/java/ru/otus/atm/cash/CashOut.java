package ru.otus.atm.cash;

import java.util.List;

public class CashOut extends AbstractCash {

    public CashOut(List<Banknote> banknotes) {
        super(banknotes);
    }

    public CashOut(Banknote banknote) {
        super(banknote);
    }
}
