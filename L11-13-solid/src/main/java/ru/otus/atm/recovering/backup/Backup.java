package ru.otus.atm.recovering.backup;

import ru.otus.atm.recovering.state.State;

public interface Backup<T extends State<?>> {

    T getState();

    void setState(T state);
}
