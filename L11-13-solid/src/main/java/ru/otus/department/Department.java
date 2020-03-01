package ru.otus.department;

import ru.otus.atm.Atm;

import java.util.ArrayList;
import java.util.List;

public class Department {
    private final List<Atm> atms = new ArrayList<>();

    public int getTotalBalance() {
        int balance = 0;
        for (Atm atm : atms) {
            balance += atm.executeCommand(Atm::getCashAmount);
        }
        return balance;
    }

    public List<Atm.Snapshot> atmsSnapshots() {
        List<Atm.Snapshot> snapshots = new ArrayList<>();
        for (Atm atm : atms) {
            snapshots.add(atm.executeCommand(Atm::makeSnapshot));
        }
        return snapshots;
    }

    public void addAtm(Atm atm) {
        atms.add(atm);
    }

    public int getAtmQuantity() {
        return atms.size();
    }
}
