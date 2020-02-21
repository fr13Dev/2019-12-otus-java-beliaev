package ru.otus.atm.backup;

import ru.otus.atm.Atm;
import ru.otus.atm.exception.IllegalAmountException;

// Memento
public class AtmState {
    private final Atm atm;

    public AtmState(Atm atm) throws IllegalAmountException {
        this.atm = new Atm(atm);
    }

    public Atm getAtm() {
        return atm;
    }
}
