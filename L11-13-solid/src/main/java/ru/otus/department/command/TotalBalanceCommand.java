package ru.otus.department.command;

import ru.otus.atm.Atm;

import java.util.List;

public class TotalBalanceCommand implements Command {

    @Override
    public int execute(List<Atm> atms) {
        return atms.stream().mapToInt(Atm::getCashAmount).sum();
    }
}
