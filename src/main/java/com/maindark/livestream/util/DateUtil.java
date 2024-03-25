package com.maindark.livestream.util;

import com.maindark.livestream.exception.GlobalException;
import com.maindark.livestream.result.CodeMsg;
import org.apache.commons.lang3.StringUtils;
import org.springframework.cglib.core.Local;

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
        return localDate.toString();
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

    public static Date convertStrToDate(String time){
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = null;
        try{
            date = dateFormat.parse(time);
        }catch (Exception e){
            throw new GlobalException(CodeMsg.SERVER_ERROR);
        }
        return date;
    }

    public static String get16Hex(){
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH,1);
        Date date = calendar.getTime();
        Long time = date.getTime();
        return Long.toHexString(time);
    }

    public static String convertStrToNormalDate(String time){
        String date = time.substring(0,9);
        String[] arr = date.split("/");
        String month = arr[1];
        if(month.length() == 1){
            arr[1] = "0" + month;
        }
        date = StringUtils.join(arr,"-");
        return date;
    }

    public static String convertStrToNormalTime(String time){
        String[] arr = time.split(" ");
        if(arr[1].length() == 8){
            return time.substring(10,15);
        } else {
            return time.substring(10,14);
        }

    }


}
