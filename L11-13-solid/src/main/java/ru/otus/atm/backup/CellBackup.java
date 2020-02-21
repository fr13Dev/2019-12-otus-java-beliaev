package ru.otus.atm.backup;

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
