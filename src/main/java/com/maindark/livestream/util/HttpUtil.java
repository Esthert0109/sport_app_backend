package com.maindark.livestream.util;


import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

@Slf4j
public class HttpUtil {

    public static String getNaMiData(String url){
        log.info("send nami url:{}",url);
        /*WebClient webClient = WebClient.create();
        WebClient.ResponseSpec responseSpec = webClient.get()
                .uri(url)
                .retrieve();
        String result = responseSpec.bodyToMono(String.class).block();*/
        RestTemplate restTemplate = new RestTemplate();
        String result = restTemplate.getForObject(url,String.class);
        return result;
    }

    public static String getAllSportsData(String url){
        log.info("send allSports url:{}",url);
        RestTemplate restTemplate = new RestTemplate();
        String result = restTemplate.getForObject(url,String.class);
        return result;
    }

    public static String sendGet(String url){
        StringBuffer stringBuffer = new StringBuffer();

        try{
            URL realUrl = new URL(url);
           HttpURLConnection httpURLConnection = (HttpURLConnection) realUrl.openConnection();
           httpURLConnection.setConnectTimeout(5000);
           httpURLConnection.setRequestMethod("GET");
           httpURLConnection.setRequestProperty("accept","*/*");
            httpURLConnection.setRequestProperty("connection", "Keep-Alive");
            httpURLConnection.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            httpURLConnection.setRequestProperty("Accept-Charset", "UTF-8");
            httpURLConnection.setRequestProperty("contentType", "UTF-8");
            httpURLConnection.setRequestProperty("content-type", "application/x-www-form-urlencoded;charset=UTF-8");
            httpURLConnection.connect();
            GZIPInputStream gzipInputStream = null;
            String encoding = httpURLConnection.getContentEncoding();
            if(encoding.equals("gzip")) {
                gzipInputStream = new GZIPInputStream(httpURLConnection.getInputStream());
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(gzipInputStream));
                String line = null;
                while((line=bufferedReader.readLine())!= null) {
                    line = new String(line.getBytes("UTF-8"));
                    stringBuffer.append(line);
                }
                bufferedReader.close();
            } else {
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
                String line = null;
                while((line = bufferedReader.readLine())!= null){
                    line = new String(line.getBytes("UTF-8"));
                    stringBuffer.append(line);
                }
                bufferedReader.close();
            }
            httpURLConnection.disconnect();
        }catch (Exception e){
            log.error("get data from feijing error:{}",e.getMessage());
        }
        return stringBuffer.toString();
    }

}
