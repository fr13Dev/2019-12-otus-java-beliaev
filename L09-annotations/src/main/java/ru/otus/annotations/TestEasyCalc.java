package ru.otus.annotations;

import ru.otus.annotations.calc.EasyCalc;
import ru.otus.annotations.testnb.AfterEach;
import ru.otus.annotations.testnb.BeforeEach;
import ru.otus.annotations.testnb.Skip;
import ru.otus.annotations.testnb.Test;
import ru.otus.annotations.testnb.exception.TestFail;

@SuppressWarnings("unused")
public class TestEasyCalc {

    @BeforeEach
    public void init() {
    }

    @AfterEach
    public void closeUp() {
    }

    @Test
    public void shouldSumTwoNumbers() throws TestFail {
        final EasyCalc calc = new EasyCalc();
        final int result = calc.sum(5, 3);
        int expected = 9;
        assertThat(expected, result);
    }

    @Test
    public void shouldDeductTwoNumbers() throws TestFail {
        final EasyCalc calc = new EasyCalc();
        final int result = calc.deduct(5, 3);
        int expected = 2;
        assertThat(expected, result);
    }

    @Skip
    @Test
    public void shouldMultiplyTwoNumbers() throws TestFail {
        final EasyCalc calc = new EasyCalc();
        final int result = calc.multiply(5, 3);
        int expected = 15;
        assertThat(expected, result);
    }

    private void assertThat(int expected, int actual) throws TestFail {
        if (actual != expected) {
            throw new TestFail(String.format("expected: %d, but was: %d", expected, actual));
        }
    }
}
