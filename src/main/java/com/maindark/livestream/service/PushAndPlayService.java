package com.maindark.livestream.service;

import com.maindark.livestream.config.PushAndPlayConfig;
import com.maindark.livestream.dao.LiveStreamDetailDao;
import com.maindark.livestream.domain.LiveStreamDetail;
import com.maindark.livestream.enums.PopularEnum;
import com.maindark.livestream.form.LiveStreamDetailForm;
import com.maindark.livestream.util.DateUtil;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class PushAndPlayService {

    @Resource
    PushAndPlayConfig pushAndPlayConfig;

    @Resource
    LiveStreamDetailDao liveStreamDetailDao;

    public Map<String,Object> getPushUrl(){
        String time = DateUtil.convertLocalDateToTime();
        Map<String,Object> map = new HashMap<>();
        String host = pushAndPlayConfig.getLiveHost();
        String pushCode = pushAndPlayConfig.getPushCode();
        map.put("time",time);
        map.put("host",host);
        map.put("code",pushCode);
        return map;
    }

    public Boolean createLiveStream(String userId, LiveStreamDetailForm liveStreamDetailForm) {
        Long id = Long.parseLong(userId);
        LiveStreamDetail liveStreamDetail = new LiveStreamDetail();
        Date date = DateUtil.convertStrToDate(liveStreamDetailForm.getCreateTime());
        liveStreamDetail.setUserId(id);
        liveStreamDetail.setLiveDate(date);
        liveStreamDetail.setCover(liveStreamDetailForm.getCover());
        liveStreamDetail.setPushHost(liveStreamDetailForm.getPushHost());
        liveStreamDetail.setPushCode(liveStreamDetailForm.getPushCode());
        liveStreamDetail.setIsPopular(PopularEnum.NO.getCode());
        liveStreamDetailDao.insertData(liveStreamDetail);
        return true;
    }
}
