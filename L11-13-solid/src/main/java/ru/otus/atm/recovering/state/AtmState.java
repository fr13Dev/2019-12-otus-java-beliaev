package ru.otus.atm.recovering.state;

import ru.otus.atm.Atm;

public class AtmState implements State<Atm> {
    private final Atm atm;

    public AtmState(Atm atm) {
        this.atm = new Atm(atm);
    }

    @Override
    public Atm get() {
        return atm;
    }
}
