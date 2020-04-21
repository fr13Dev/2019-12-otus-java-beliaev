package ru.otus;

import java.util.concurrent.TimeUnit;

public class FirstCounter extends AbstractCounter implements Runnable {

    public FirstCounter(Printer printer) {
        super(printer);
    }

    @Override
    public void run() {
        try {
            while (!Thread.interrupted()) {
                processCurrentStep();
                TimeUnit.MILLISECONDS.sleep(500);
                printer.printFirst();
                printer.waitForPrintSecond();
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.println(e.getMessage());
        }
    }
}
