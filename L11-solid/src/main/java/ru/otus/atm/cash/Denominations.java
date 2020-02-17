package ru.otus.atm.cash;

import ru.otus.atm.exception.NoSuchDenominationException;

public enum Denominations {
    ONE_THOUSAND(1_000),
    SEVEN_HUNDRED(700);

    private int denomination;

    Denominations(int denomination) {
        this.denomination = denomination;
    }

    public int getDenomination() {
        return denomination;
    }

    public static Denominations instanceOf(int denomination) throws NoSuchDenominationException {
        switch (denomination) {
            case 1_000:
                return ONE_THOUSAND;
            case 700:
                return SEVEN_HUNDRED;
            default:
                throw new NoSuchDenominationException(denomination);
        }
    }
}
