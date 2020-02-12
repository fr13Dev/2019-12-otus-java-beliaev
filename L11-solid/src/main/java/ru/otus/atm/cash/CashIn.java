package ru.otus.atm.cash;

import java.util.List;

public class CashIn extends AbstractCash {

    public CashIn(List<Banknote> banknotes) {
        super(banknotes);
    }

    public CashIn(Banknote banknote) {
        super(banknote);
    }
}
