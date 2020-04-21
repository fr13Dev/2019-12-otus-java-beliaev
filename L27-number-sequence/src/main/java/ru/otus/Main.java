package ru.otus;

import java.util.concurrent.TimeUnit;

public class Main {

    public static void main(String[] args) throws InterruptedException {
        var printer = new Printer();

        var firstThread = new Thread(new FirstCounter(printer));
        firstThread.setName("first");
        firstThread.setDaemon(true);

        var secondThread = new Thread(new SecondCounter(printer));
        secondThread.setName("second");
        secondThread.setDaemon(true);
        firstThread.start();
        secondThread.start();

        TimeUnit.MINUTES.sleep(1);
    }
}
