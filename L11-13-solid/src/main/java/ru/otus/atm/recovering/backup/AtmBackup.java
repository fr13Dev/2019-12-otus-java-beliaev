package ru.otus.atm.recovering.backup;

import ru.otus.atm.recovering.state.AtmState;

public class AtmBackup implements Backup<AtmState> {
    private AtmState atmState;

    @Override
    public AtmState getState() {
        return atmState;
    }

    @Override
    public void setState(AtmState state) {
        this.atmState = state;
    }
}
