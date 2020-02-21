package ru.otus.atm.backup;

import ru.otus.atm.storage.CashCell;

// Memento
public class CellState {
    private final CashCell cell;

    public CellState(CashCell cell) {
        this.cell = new CashCell(cell);
    }

    public CashCell getCell() {
        return cell;
    }
}
