package com.maindark.livestream;

import com.maindark.livestream.util.DateUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Date;

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
        LocalDate now = LocalDate.now();
        //jintian yi jieshu mingtian wei kaisai  zuotian yijieshu
        LocalDate tomorrow = now.plusDays(1);
        LocalDate future = now.plusDays(2);
        LocalDate past = now.minusDays(1);
        long nowSeconds = DateUtil.convertDateToLongTime(now);
        long tomorrowSeconds = DateUtil.convertDateToLongTime(tomorrow);
        long futureSeconds = DateUtil.convertDateToLongTime(future);
        long pastSeconds = DateUtil.convertDateToLongTime(past);
        System.out.println(pastSeconds);
        System.out.println(nowSeconds);
        System.out.println(tomorrowSeconds);
        System.out.println(futureSeconds);

//        String str = "23^5-11^3-7^0-0^0^0^0^2^1^0^1^4^0^13^1^1^0";
//        String[] dataArr = str.split("^",17);
//        for(String data:dataArr){
//            System.out.println(data);
//        }
        String str = "33^11-15^0-0^5-7^4^6^10^3^2^1^4^6^-8^27^1^1^0";
        String[] dataArr = str.split("[/^]+",19);
        if(dataArr != null && dataArr.length >0) {
            String minutes = dataArr[0];
            String fieldGoalsAttempts = dataArr[1].split("-",2)[0];
            String fieldGoalsMade = dataArr[1].split("-",2)[1];
            String threePointGoalsAttempts = dataArr[2].split("-",2)[0];
            String threePointGoalsMade = dataArr[2].split("-",2)[1];
            String freeThrowsGoalsAttempts = dataArr[3].split("-",2)[0];
            String freeThrowsGoalsMade = dataArr[3].split("-",2)[1];
            String totalRebounds = dataArr[6];
            String assists = dataArr[7];
            String steals = dataArr[8];
            String blocks = dataArr[9];
            String turnovers = dataArr[10];
            String personalFouls = dataArr[11];
            String point = dataArr[13];
        }
        for(String dd:dataArr){
            System.out.println(dd);
        }

    }
}
