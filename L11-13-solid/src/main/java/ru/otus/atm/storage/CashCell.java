package ru.otus.atm.storage;

import ru.otus.atm.cash.Banknote;
import ru.otus.atm.exception.IllegalAmountException;
import ru.otus.atm.recovering.Recovering;
import ru.otus.atm.recovering.state.CellState;

import java.util.ArrayList;
import java.util.List;


public class CashCell implements Cell, Recovering<CellState> {
    private final Banknote baseBanknote;
    private int quantity;

    public CashCell(Banknote baseBanknote) {
        this.baseBanknote = baseBanknote;
    }

    public CashCell(CashCell cell) {
        baseBanknote = cell.getBaseBanknote();
        quantity = cell.getBanknotesQuantity();
    }

    @Override
    public Banknote getBaseBanknote() {
        return baseBanknote;
    }

    @Override
    public int getBanknotesQuantity() {
        return quantity;
    }

    @Override
    public void decrementBanknoteQuantity() {
        if (quantity != 0) {
            quantity--;
        }
    }

    @Override
    public void addBanknote() {
        quantity++;
    }

    @Override
    public void addBanknotes(int quantity) {
        this.quantity += quantity;
    }

    @Override
    public List<Banknote> getBanknotes(int quantity) throws IllegalAmountException {
        if (this.quantity < quantity) {
            throw new IllegalAmountException(String.format("Can not to issue %d of banknotes %h", quantity, baseBanknote));
        }
        this.quantity -= quantity;
        final List<Banknote> banknotes = new ArrayList<>();
        for (int i = 0; i < quantity; i++) {
            banknotes.add(new Banknote(baseBanknote.getDenomination()));
        }
        return banknotes;
    }

    @Override
    public void load(CellState state) {
        quantity = state.get().getBanknotesQuantity();
    }

    @Override
    public CellState save() {
        return new CellState(this);
    }
}
