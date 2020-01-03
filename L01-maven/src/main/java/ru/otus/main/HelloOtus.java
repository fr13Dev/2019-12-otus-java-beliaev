package ru.otus.main;

import com.google.common.base.Splitter;

import java.util.Scanner;

public class HelloOtus {
    public static void main(String[] args) {
        System.out.println("Type some words...");
        Scanner s = new Scanner(System.in);
        String str = s.nextLine();
        final int length = 1;
        Splitter.fixedLength(length)
                .trimResults()
                .split(str)
                .forEach(System.out::println);
    }
}
