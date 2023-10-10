package com.maindark.livestream.util;


import lombok.extern.slf4j.Slf4j;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

@Slf4j
public class HttpUtil {

    public static String getNaMiData(String url){
        log.info("send nami url:{}",url);
        WebClient webClient = WebClient.create();
        WebClient.ResponseSpec responseSpec = webClient.get()
                .uri(url)
                .retrieve();
        String result = responseSpec.bodyToMono(String.class).block();
       // RestTemplate restTemplate = new RestTemplate();
       // String result = restTemplate.getForObject(url,String.class);
        return result;
    }
}
