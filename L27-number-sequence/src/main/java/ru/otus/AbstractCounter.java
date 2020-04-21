package ru.otus;

public class AbstractCounter {
    protected final Printer printer;
    protected static final int LIMIT = 10;
    protected Direction direction = Direction.FORWARD;
    protected int currentStep = 1;

    public AbstractCounter(Printer printer) {
        this.printer = printer;
    }

    protected void processCurrentStep() {
        if (currentStep % LIMIT == 0) {
            direction = direction == Direction.FORWARD ? Direction.BACKWARD : Direction.FORWARD;
        }
        System.out.println(String.format("%s: %d", Thread.currentThread().getName(), currentStep));
        if (direction == Direction.FORWARD) {
            currentStep++;
        } else {
            currentStep--;
        }
    }
}
