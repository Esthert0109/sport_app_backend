package com.maindark.livestream.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class DateUtil {
    public static Long convertDateToLongTime(LocalDate localDate){
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String currentDate = localDate.toString();
        long nowSeconds = LocalDate.parse(currentDate, dateFormatter)
                .atStartOfDay(ZoneOffset.UTC)
                .toInstant()
                .toEpochMilli() / 1000;
        return nowSeconds;
    }

    public static String interceptTime(Long time) {
        Date sol = new Date(time);
        DateFormat obj = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");
        String timeStr = obj.format(sol);
        timeStr = timeStr.substring(11,16);
        return timeStr;
    }

    public static LocalDate convertStringToDate(String str){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        LocalDate date = LocalDate.parse(str,formatter);
        return date;
    }

    public static String convertDateToStr(LocalDate localDate){
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String currentDate = localDate.toString();
        String str = LocalDate.parse(currentDate,dateFormatter).toString();
        return str;
    }

    public static String convertLongTimeToMatchDate(Long time) {
        Date sol = new Date(time);
        DateFormat obj = new SimpleDateFormat("yyyy-MM-dd");
        String timeStr = obj.format(sol);
        return timeStr;
    }



}
