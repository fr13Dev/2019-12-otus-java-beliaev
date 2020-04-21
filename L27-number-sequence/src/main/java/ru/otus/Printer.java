package ru.otus;

public class Printer {
    private boolean firstPrinted;
    private boolean secondPrinted;

    public synchronized void printFirst() {
        firstPrinted = true;
        secondPrinted = false;
        notifyAll();
    }

    public synchronized void printSecond() {
        secondPrinted = true;
        firstPrinted = false;
        notifyAll();
    }

    public synchronized void waitForPrintFirst() throws InterruptedException {
        while (!firstPrinted) {
            wait();
        }
    }

    public synchronized void waitForPrintSecond() throws InterruptedException {
        while (!secondPrinted) {
            wait();
        }
    }
}
