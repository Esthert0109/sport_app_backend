package com.maindark.livestream.task;

import com.maindark.livestream.allSports.AllSportsConfig;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@EnableScheduling
public class AllSportsBasketballLiveDataTask {
    @Resource
    AllSportsConfig allSportsConfig;
}
