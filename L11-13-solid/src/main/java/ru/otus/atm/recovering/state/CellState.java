package ru.otus.atm.recovering.state;

import ru.otus.atm.storage.CashCell;

public class CellState implements State<CashCell> {
    private final CashCell cell;

    public CellState(CashCell cell) {
        this.cell = new CashCell(cell);
    }

    @Override
    public CashCell get() {
        return cell;
    }
}
