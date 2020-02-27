package ru.otus.atm.command;

import ru.otus.atm.Atm;

@FunctionalInterface
public interface Command {

    int execute(Atm atm);
}
