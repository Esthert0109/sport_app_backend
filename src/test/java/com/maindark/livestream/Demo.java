package com.maindark.livestream;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class Demo {
    public static void main(String[] args) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        LocalDate date = LocalDate.parse("20180505",formatter);
        System.out.println(date);
    }
}
