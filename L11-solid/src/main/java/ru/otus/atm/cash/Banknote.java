package ru.otus.atm.cash;

import ru.otus.atm.exception.NoSuchDenominationException;

import java.util.Objects;

public class Banknote {
    private final Denominations denomination;

    public Banknote(Denominations denomination) {
        this.denomination = denomination;
    }

    public Banknote(int denomination) {
        try {
            this.denomination = Denominations.instanceOf(denomination);
        } catch (NoSuchDenominationException e) {
            throw new IllegalArgumentException(e);
        }
    }

    public int getDenomination() {
        return denomination.getDenomination();
    }

    @Override
    public String toString() {
        return "Banknote{" +
                "denomination=" + denomination +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Banknote banknote = (Banknote) o;
        return denomination == banknote.denomination;
    }

    @Override
    public int hashCode() {
        return Objects.hash(denomination);
    }
}
