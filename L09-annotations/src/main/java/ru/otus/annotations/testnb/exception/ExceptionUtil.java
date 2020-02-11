package ru.otus.annotations.testnb.exception;

public class ExceptionUtil {

    public static TestFail convert(Exception e) {
        if (e.getCause().getClass().equals(TestFail.class)) {
            return new TestFail(" - fail, reason: " + e.getCause().getMessage() + ".", e);
        } else {
            return new TestFail(e);
        }
    }

    public ExceptionUtil() {
        throw new UnsupportedOperationException();
    }
}
