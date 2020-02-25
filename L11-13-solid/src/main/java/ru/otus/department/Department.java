package ru.otus.department;

import ru.otus.atm.Atm;
import ru.otus.atm.command.TotalBalanceCommand;
import ru.otus.atm.recovering.backup.AtmBackup;
import ru.otus.atm.recovering.backup.Backup;
import ru.otus.atm.recovering.state.AtmState;

import java.util.ArrayList;
import java.util.List;

public class Department {
    private final List<Atm> atms = new ArrayList<>();

    public int getTotalBalance() {
        int balance = 0;
        for (Atm atm : atms) {
            final TotalBalanceCommand command = new TotalBalanceCommand(atm);
            balance += command.execute();
        }
        return balance;
    }

    public void addAtm(Atm atm) {
        atms.add(atm);
    }

    public int getAtmQuantity() {
        return atms.size();
    }

    public List<Backup<AtmState>> saveAtmStates() {
        final List<Backup<AtmState>> backups = new ArrayList<>();
        for (Atm i : atms) {
            final Backup<AtmState> backup = new AtmBackup();
            backup.setState(i.save());
            backups.add(backup);
        }
        return backups;
    }

    public void restoreAtmStates(List<Backup<AtmState>> backups) {
        atms.clear();
        backups.forEach(backup -> atms.add(backup.getState().get()));
    }
}
