package ru.otus.department;

import ru.otus.atm.Atm;
import ru.otus.atm.recovering.backup.AtmBackup;
import ru.otus.atm.recovering.backup.Backup;
import ru.otus.atm.recovering.state.AtmState;
import ru.otus.department.command.Command;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Department {
    private final List<Atm> atms = new ArrayList<>();
    private final Command totalBalance;

    public Department(Command totalBalance) {
        this.totalBalance = totalBalance;
    }

    public int getTotalBalance() {
        return totalBalance.execute(Collections.unmodifiableList(atms));
    }

    public void addAtm(Atm atm) {
        atms.add(atm);
    }

    public int getAtmQuantity() {
        return atms.size();
    }

    public List<Backup<AtmState>> saveAtmStates() {
        final List<Backup<AtmState>> backups = new ArrayList<>();
        atms.forEach(i -> {
            final Backup<AtmState> backup = new AtmBackup();
            backup.setState(i.save());
            backups.add(backup);
        });
        return backups;
    }

    public void restoreAtmStates(List<Backup<AtmState>> backups) {
        atms.clear();
        backups.forEach(backup -> atms.add(backup.getState().get()));
    }
}
