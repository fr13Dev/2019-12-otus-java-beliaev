package ru.otus.gc.bench;

import java.util.ArrayList;
import java.util.List;

public class Benchmark implements BenchmarkMBean {
    private final List<Integer> storage = new ArrayList<>();

    public void run() throws InterruptedException {

        while (true) {
            for (int i = 0; i < 1_000_000; i++) {
                storage.add(i);
            }
            Thread.sleep(5_000);
            storage.removeIf(i -> i % 2 == 0);
            Thread.sleep(1_000);
        }
    }
}
