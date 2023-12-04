package com.maindark.livestream.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
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
        return LocalDate.parse(str,formatter);
    }

    public static String convertDateToStr(LocalDate localDate){
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String currentDate = localDate.toString();
        return LocalDate.parse(currentDate,dateFormatter).toString();
    }

    public static String convertLongTimeToMatchDate(Long time) {
        Date sol = new Date(time);
        DateFormat obj = new SimpleDateFormat("yyyy-MM-dd");
        return obj.format(sol);
    }

    public static String convertLocalDateToStr(LocalDate localDate){
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        String currentDate = localDate.toString();
        return LocalDate.parse(currentDate,dateFormatter).toString();
    }

    public static String convertLocalDateToTime(){
        Date date = new Date();
        DateFormat obj = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return obj.format(date);
    }


    public static Long convertDateToLongTime(){
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH,1);
        Date date = calendar.getTime();
        return date.getTime() / 1000;
    }

    public static String get16Hex(){
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH,1);
        Date date = calendar.getTime();
        Long time = date.getTime();
        return Long.toHexString(time);
    }


}
