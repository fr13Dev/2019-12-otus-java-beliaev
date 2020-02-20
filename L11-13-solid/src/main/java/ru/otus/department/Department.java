package ru.otus.department;

import ru.otus.atm.Atm;
import ru.otus.department.command.Command;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Department {
    private List<Atm> atms = new ArrayList<>();
    private final Command totalBalance;

    public Department(Command totalBalance) {
        this.totalBalance = totalBalance;
    }

    public int getTotalBalance() {
        return totalBalance.execute(Collections.unmodifiableList(atms));
    }

    public List<Atm> getAtms() {
        return Collections.unmodifiableList(atms);
    }

    public void addAtm(Atm atm) {
        atms.add(atm);
    }

    public int getAtmQuantity() {
        return atms.size();
    }
}
