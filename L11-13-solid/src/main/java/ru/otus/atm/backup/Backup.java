package ru.otus.atm.backup;

public interface Backup<T> {

    T getState();

    void setState(T state);
}
