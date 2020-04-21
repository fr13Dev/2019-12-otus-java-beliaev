package ru.otus;

import java.util.concurrent.TimeUnit;

public class SecondCounter extends AbstractCounter implements Runnable {

    public SecondCounter(Printer printer) {
        super(printer);
    }

    @Override
    public void run() {
        try {
            while (!Thread.interrupted()) {
                printer.waitForPrintFirst();
                processCurrentStep();
                TimeUnit.MILLISECONDS.sleep(200);
                printer.printSecond();
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.println(e.getMessage());
        }
    }
}
