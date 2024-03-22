package com.maindark.livestream.service;

import com.maindark.livestream.config.PushAndPlayConfig;
import com.maindark.livestream.dao.AnchorFollowDao;
import com.maindark.livestream.dao.LiveStreamDetailDao;
import com.maindark.livestream.domain.LiveStreamDetail;
import com.maindark.livestream.enums.PopularEnum;
import com.maindark.livestream.form.LiveStreamDetailForm;
import com.maindark.livestream.util.DateUtil;
import com.maindark.livestream.vo.LiveStreamDetailVo;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class PushAndPlayService {

    @Resource
    PushAndPlayConfig pushAndPlayConfig;

    @Resource
    LiveStreamDetailDao liveStreamDetailDao;

    @Resource
    AnchorFollowDao anchorFollowDao;

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

    public Integer createLiveStream(String userId, LiveStreamDetailForm liveStreamDetailForm) {
        Long id = Long.parseLong(userId);
        LiveStreamDetail liveStreamDetail = new LiveStreamDetail();
        Date date = DateUtil.convertStrToDate(liveStreamDetailForm.getCreateTime());
        liveStreamDetail.setUserId(id);
        liveStreamDetail.setLiveDate(date);
        liveStreamDetail.setCover(liveStreamDetailForm.getCover());
        liveStreamDetail.setPushHost(liveStreamDetailForm.getPushHost());
        liveStreamDetail.setPushCode(liveStreamDetailForm.getPushCode());
        liveStreamDetail.setIsPopular(PopularEnum.NO.getCode());
        liveStreamDetail.setTitle(liveStreamDetailForm.getTitle());
        liveStreamDetail.setSportType(liveStreamDetailForm.getSportType());
        liveStreamDetailDao.insertData(liveStreamDetail);

//        update follow table
        anchorFollowDao.updateStreamingStatusById(id, true);

        return liveStreamDetail.getId();
    }

    public LiveStreamDetailVo getLiveStreamDetailById(Integer id) {
        return liveStreamDetailDao.getLiveStreamDetailById(id);
    }

    public void updateLiveStreamDetailById(Integer id,LiveStreamDetailForm liveStreamDetailForm) {
        LiveStreamDetail liveStreamDetail = new LiveStreamDetail();
        liveStreamDetail.setId(id);
        liveStreamDetail.setTitle(liveStreamDetailForm.getTitle());
        liveStreamDetail.setCover(liveStreamDetailForm.getCover());
        liveStreamDetailDao.updateLiveStreamDetailById(liveStreamDetail);
    }

    public List<LiveStreamDetailVo> getAllPopularLiveStreamDetails(Pageable pageable) {
        int pageSize = pageable.getPageSize();
        long offset = pageable.getOffset();
        return liveStreamDetailDao.getAllPopularLiveStreamDetails(pageSize,offset);
    }

    public List<LiveStreamDetailVo> getAllLiveStreamDetails(Pageable pageable) {
        int pageSize = pageable.getPageSize();
        long offset = pageable.getOffset();
        return liveStreamDetailDao.getAllLiveStreamDetails(pageSize,offset);
    }

    public List<LiveStreamDetailVo> getAllLiveStreamDetailsBySportType(Pageable pageable,String sportType) {
        int pageSize = pageable.getPageSize();
        long offset = pageable.getOffset();
        return liveStreamDetailDao.getAllLiveStreamDetailsBySportType(sportType,pageSize,offset);
    }

    public void deleteLiveRoomById(Integer id) {
        liveStreamDetailDao.deleteLiveStreamDetailById(id);

        //        update follow table
        LiveStreamDetailVo liveStreamDetailVo = liveStreamDetailDao.getLiveStreamDetailById(id);
        anchorFollowDao.updateStreamingStatusById(liveStreamDetailVo.getUserId(), true);

    }

    public LiveStreamDetailVo getLiveStreamDetailsByUserId(Long userId) {
        return liveStreamDetailDao.getLiveStreamDetailByAnchorId(userId);
    }
}
