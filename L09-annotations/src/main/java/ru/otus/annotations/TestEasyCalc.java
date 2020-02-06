package ru.otus.annotations;

import ru.otus.annotations.calc.EasyCalc;
import ru.otus.annotations.testnb.Skip;
import ru.otus.annotations.testnb.Test;

public class TestEasyCalc {

    @Test
    public void shouldSumTwoNumbers() {
        final EasyCalc calc = new EasyCalc();
        final int result = calc.sum(5, 3);
        int expected = 8;
        if (result != expected) {
            throw new IllegalStateException("wrong");
        }
    }

    @Skip
    @Test
    public void shouldMultiplyTwoNumbers() {
        final EasyCalc calc = new EasyCalc();
        final int result = calc.multiply(5, 3);
        int expected = 8;
        if (result != expected) {
            throw new IllegalStateException("wrong");
        }
    }
}
