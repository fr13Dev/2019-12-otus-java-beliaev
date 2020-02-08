package ru.otus.annotations.testnb.matcher;

import ru.otus.annotations.testnb.exception.TestFail;

public class Assert {

    public static void assertThat(Object expected, Object actual) throws TestFail {
        if (!expected.equals(actual)) {
            throw new TestFail(String.format("expected: %h, but was: %h", expected, actual));
        }
    }
}