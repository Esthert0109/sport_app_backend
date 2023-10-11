package com.maindark.livestream;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.LocalDate;
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

    public static void main(String[] args) throws Exception {
        liveRtmpFeed();
    }
}
