package ru.otus.department;

import ru.otus.atm.Atm;
import ru.otus.atm.command.GetBalanceCommand;

import java.util.ArrayList;
import java.util.List;

public class Department {
    private final List<Atm> atms = new ArrayList<>();

    public int getTotalBalance() {
        int balance = 0;
        for (Atm atm : atms) {
            final GetBalanceCommand command = new GetBalanceCommand();
            balance += command.execute(atm);
        }
        return balance;
    }

    public void addAtm(Atm atm) {
        atms.add(atm);
    }

    public int getAtmQuantity() {
        return atms.size();
    }
}
