package ru.otus.atm.exception;

public class NoSuchDenominationException extends Exception {

    public NoSuchDenominationException(int denomination) {
        super(String.format("No such denomination %h", denomination));
    }
}
