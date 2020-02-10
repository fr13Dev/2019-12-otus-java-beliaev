package ru.otus.annotations.testnb.exception;

public class TestFail extends Exception {

    public TestFail(String message) {
        super(message);
    }

    public TestFail(String message, Throwable cause) {
        super(message, cause);
    }

    public TestFail(Throwable cause) {
        super(cause);
    }
}
