package ru.otus.annotations;

import ru.otus.annotations.calc.EasyCalc;
import ru.otus.annotations.testnb.AfterEach;
import ru.otus.annotations.testnb.BeforeEach;
import ru.otus.annotations.testnb.Skip;
import ru.otus.annotations.testnb.Test;
import ru.otus.annotations.testnb.exception.TestFail;
import ru.otus.annotations.testnb.matcher.Assert;

@SuppressWarnings("unused")
public class TestEasyCalc {
    private final EasyCalc calc = new EasyCalc();

    @BeforeEach
    public void init() {
    }

    @AfterEach
    public void closeUp() {
    }

    @Test
    public void shouldSumTwoNumbers() throws TestFail {
        final int result = calc.sum(5, 3);
        int expected = 9;
        Assert.assertThat(expected, result);
    }

    @Test
    public void shouldDeductTwoNumbers() throws TestFail {
        final int result = calc.deduct(5, 3);
        int expected = 2;
        Assert.assertThat(expected, result);
    }

    @Skip
    @Test
    public void shouldMultiplyTwoNumbers() throws TestFail {
        final int result = calc.multiply(5, 3);
        int expected = 15;
        Assert.assertThat(expected, result);
    }
}
