package com.maindark.livestream;

import com.maindark.livestream.util.DateUtil;

import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class Demo {
    public static void liveRtmpFeed() throws IOException, InterruptedException {
        ProcessBuilder processBuilder = new ProcessBuilder("ffmpeg", "-i",
                "rtmp://www.mindark.cloud/live/sd-1-3911620", "-ss", "00:00:00",
                "-t", "00:00:10", "-c", "copy", "/home/outputVideo.mp4");
        Process process = processBuilder.start();
        InputStream stderr = process.getErrorStream();
        InputStreamReader isr = new InputStreamReader(stderr);
        BufferedReader br = new BufferedReader(isr);
        while ((br.readLine()) != null)
            ;
        process.waitFor();

        try {
            process.destroy();
            isr.close();
            stderr.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static Long convertDateToLongTime(LocalDate localDate){
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String currentDate = localDate.toString();
        long nowSeconds = LocalDate.parse(currentDate, dateFormatter)
                .atStartOfDay(ZoneOffset.UTC)
                .toInstant()
                .toEpochMilli() / 1000;
        return nowSeconds;
    }

    public static void main(String[] args) throws Exception {
    /*    LocalDate now = LocalDate.now();
        //jintian yi jieshu mingtian wei kaisai  zuotian yijieshu
        LocalDate tomorrow = now.plusDays(1);
        LocalDate future = now.plusDays(2);
        LocalDate past = now.minusDays(2);
        long nowSeconds = DateUtil.convertDateToLongTime(now);
        long tomorrowSeconds = DateUtil.convertDateToLongTime(tomorrow);
        long tomorrowSeconds1 = DateUtil.convertDateToLongTime();
        long futureSeconds = DateUtil.convertDateToLongTime(future);
        long pastSeconds = DateUtil.convertDateToLongTime(past);*/
        LocalDate localDate = LocalDate.now();
        String from = DateUtil.convertLocalDateToStr(localDate);
        //System.out.println(pastSeconds);
       /* System.out.println(nowSeconds);
        System.out.println(tomorrowSeconds);
        System.out.println(tomorrowSeconds1);*/
        //System.out.println(futureSeconds);
//        DateFormat obj = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        Date date = obj.parse("2023-12-05 10:36:45");
//        LocalDate localDate = LocalDate.parse("2023-12-05 10:36:45");
//        System.out.println(date.toString());
//        System.out.println(localDate.toString());
        //System.out.println(from);

        String time = "2024/3/12 9:59:14";
        String date = DateUtil.convertStrToNormalDate(time);
        String str = DateUtil.convertStrToNormalTime(time);
        System.out.println(date);
        System.out.println(str);
    }


}
