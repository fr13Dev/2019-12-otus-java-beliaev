package ru.otus.department.command;

import ru.otus.atm.Atm;

import java.util.List;

@FunctionalInterface
public interface Command {

    int execute(List<Atm> atms);
}
