package ru.otus.atm.recovering.backup;

import ru.otus.atm.recovering.state.CellState;

// Caretaker
public class CellBackup implements Backup<CellState> {
    private CellState cellState;

    @Override
    public CellState getState() {
        return cellState;
    }

    @Override
    public void setState(CellState cellState) {
        this.cellState = cellState;
    }
}
