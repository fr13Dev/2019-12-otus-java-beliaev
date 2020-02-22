package ru.otus.atm.storage;

import org.junit.Test;
import ru.otus.atm.AbstractTest;
import ru.otus.atm.cash.Banknote;
import ru.otus.atm.exception.IllegalAmountException;
import ru.otus.atm.recovering.backup.Backup;
import ru.otus.atm.recovering.backup.CellBackup;
import ru.otus.atm.recovering.state.CellState;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class CashCellTest extends AbstractTest {
    private final Cell cell = new CashCell(ONE_THOUSAND);

    @Test
    public void addSomeBanknotesAndReturnQuantity() {
        cell.addBanknotes(5);
        assertEquals(5, cell.getBanknotesQuantity());
    }

    @Test
    public void addSomeBanknotesAndGetPartOfThem() throws IllegalAmountException {
        cell.addBanknotes(5);
        final Banknote baseBanknote = cell.getBaseBanknote();
        final List<Banknote> banknotes = cell.getBanknotes(3);
        assertEquals(3, banknotes.size());
        assertEquals(2, cell.getBanknotesQuantity());
        banknotes.forEach(i -> assertEquals(i, baseBanknote));
    }

    @Test(expected = IllegalAmountException.class)
    public void addSomeBanknotesAndGetInvalidQuantity() throws IllegalAmountException {
        cell.addBanknotes(5);
        cell.getBanknotes(10);
    }

    @Test
    public void addSomeBanknotesGetAllOfThemAndRestoreState() {
        cell.addBanknotes(5);
        final Backup<CellState> cellBackup = new CellBackup();
        cellBackup.setState(((CashCell) cell).save());
        cell.decrementBanknoteQuantity();
        ((CashCell) cell).load(cellBackup.getState());
        assertEquals(5, cell.getBanknotesQuantity());
    }
}