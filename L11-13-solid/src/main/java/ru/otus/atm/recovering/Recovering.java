package ru.otus.atm.recovering;

import ru.otus.atm.recovering.state.State;

public interface Recovering<T extends State<?>> {

    void load(T state);

    T save();
}
