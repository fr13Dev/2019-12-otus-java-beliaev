package ru.otus.atm.command;

import ru.otus.atm.Atm;

public class TotalBalanceCommand implements Command {
    private final Atm atm;

    public TotalBalanceCommand(Atm atm) {
        this.atm = atm;
    }

    @Override
    public int execute() {
        return atm.getCashAmount();
    }
}
