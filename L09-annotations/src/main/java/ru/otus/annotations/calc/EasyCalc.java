package ru.otus.annotations.calc;

public class EasyCalc {

    public int sum(int a, int b) {
        return a + b;
    }

    public int deduct(int a, int b) {
        return a - b;
    }

    public int multiply(int a, int b) {
        return a * b;
    }

    public double divide(int a, int b) {
        if (b == 0) {
            throw new ArithmeticException("divide by zero");
        }
        return (double) a / b;
    }
}
