package ru.otus.atm.command;

import ru.otus.atm.Atm;

public class GetBalanceCommand implements Command {

    @Override
    public int execute(Atm atm) {
        return atm.getCashAmount();
    }
}
