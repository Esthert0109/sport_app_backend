package com.maindark.livestream.service;

import com.maindark.livestream.dao.FeiJingInforDao;
import com.maindark.livestream.domain.feijing.FeiJingInfor;
import com.maindark.livestream.domain.feijing.InfoCategory;
import jakarta.annotation.Resource;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class FeiJingInfoService {

    @Resource
    FeiJingInforDao feiJingInforDao;


    public List<FeiJingInfor> getInfoList(String search, Pageable pageable) {
        int pageSize = pageable.getPageSize();
        long offset = pageable.getOffset();
        Map<String,Object> searchMap = new HashMap<>();
        searchMap.put("pageSize",pageSize);
        searchMap.put("offset",offset);
        searchMap.put("search",search);
        List<FeiJingInfor> list = feiJingInforDao.selectFeiJingInforList(searchMap);
        return list;
    }

    public List<InfoCategory> getCategories() {
        return feiJingInforDao.getCategories();
    }


    public FeiJingInfor getInfoById(Integer id) {
        return feiJingInforDao.getInfoById(id);
    }

    public List<FeiJingInfor> getPopularList(Integer categoryId, Pageable pageable) {
        int pageSize = pageable.getPageSize();
        long offset = pageable.getOffset();
        Map<String,Object> searchMap = new HashMap<>();
        searchMap.put("pageSize",pageSize);
        searchMap.put("offset",offset);
        searchMap.put("search",categoryId);
        List<FeiJingInfor> list = feiJingInforDao.selectFeiJingInforPopularList(searchMap);
        return list;
    }
}
