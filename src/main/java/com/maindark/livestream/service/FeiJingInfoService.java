package com.maindark.livestream.service;

import com.maindark.livestream.dao.FeiJingInforDao;
import com.maindark.livestream.domain.feijing.FeiJingInfor;
import com.maindark.livestream.domain.feijing.InfoCategory;
import com.maindark.livestream.enums.EntityTypeEnum;
import com.maindark.livestream.enums.PopularEnum;
import com.maindark.livestream.util.StreamToListUtil;
import com.maindark.livestream.vo.FeiJingInfoVo;
import jakarta.annotation.Resource;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

@Service
public class FeiJingInfoService {

    @Resource
    FeiJingInforDao feiJingInforDao;

    @Resource
    FollowService followService;


    public List<FeiJingInfoVo> getInfoList(String search, Pageable pageable) {
        int pageSize = pageable.getPageSize();
        long offset = pageable.getOffset();
        Map<String,Object> searchMap = new HashMap<>();
        searchMap.put("pageSize",pageSize);
        searchMap.put("offset",offset);
        searchMap.put("search",search);
        List<FeiJingInfoVo> list = feiJingInforDao.selectFeiJingInforList(searchMap);
        Stream<FeiJingInfoVo> stream = list.stream().peek(info ->{
            long count = followService.findFollowerCount(EntityTypeEnum.INFO.getCode(), info.getId());
            info.setReadCount((int) count);
        });
        list = StreamToListUtil.getArrayListFromStream(stream);
        return list;
    }

    public List<InfoCategory> getCategories() {
        return feiJingInforDao.getCategories();
    }


    public FeiJingInfor getInfoById(Integer id,Long userId) {
        if(userId != null) {
            followService.follow(userId.intValue(), EntityTypeEnum.INFO.getCode(),id);
        }
        return feiJingInforDao.getInfoById(id);
    }

    public List<FeiJingInfoVo> getPopularList(Integer categoryId, Pageable pageable) {
        int pageSize = pageable.getPageSize();
        long offset = pageable.getOffset();
        Map<String,Object> searchMap = new HashMap<>();
        searchMap.put("pageSize",pageSize);
        searchMap.put("offset",offset);
        searchMap.put("search",categoryId);
        List<FeiJingInfoVo> list = feiJingInforDao.selectFeiJingInforPopularList(searchMap);
        Stream<FeiJingInfoVo> stream = list.stream().peek(info ->{
            long count = followService.findFollowerCount(EntityTypeEnum.INFO.getCode(), info.getId());
            info.setReadCount((int) count);
        });
        list = StreamToListUtil.getArrayListFromStream(stream);
        return list;
    }

    public List<FeiJingInfoVo> getInfoTopList(String search, Pageable pageable) {
        int pageSize = pageable.getPageSize();
        long offset = pageable.getOffset();
        Map<String,Object> searchMap = new HashMap<>();
        searchMap.put("pageSize",pageSize);
        searchMap.put("offset",offset);
        searchMap.put("search",search);
        searchMap.put("isTop", PopularEnum.YES.getCode());
        List<FeiJingInfoVo> list = feiJingInforDao.selectFeiJingInforTopList(searchMap);
        Stream<FeiJingInfoVo> stream = list.stream().peek(info ->{
            long count = followService.findFollowerCount(EntityTypeEnum.INFO.getCode(), info.getId());
            info.setReadCount((int) count);
        });
        list = StreamToListUtil.getArrayListFromStream(stream);
        return list;
    }
}
