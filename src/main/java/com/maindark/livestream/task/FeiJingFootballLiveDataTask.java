package com.maindark.livestream.task;

import com.maindark.livestream.dao.FeiJingFootballMatchDao;
import com.maindark.livestream.feiJing.FeiJingConfig;
import com.maindark.livestream.util.HttpUtil;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@EnableScheduling
public class FeiJingFootballLiveDataTask {
    @Resource
    FeiJingConfig feiJingConfig;




}
