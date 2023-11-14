package com.maindark.livestream.service;

import com.maindark.livestream.dao.PopularSearchDao;
import com.maindark.livestream.domain.PopularSearch;
import com.maindark.livestream.form.PopularSearchForm;
import com.maindark.livestream.vo.PopularSearchVo;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class PopularSearchService {

    @Resource
    PopularSearchDao popularSearchDao;

    public void createPopularSearch(PopularSearchForm popularSearchForm){
        PopularSearch popularSearch = new PopularSearch();
        popularSearch.setPopularKeywords(popularSearchForm.getPopularKeywords());
        popularSearch.setCreateDate(new Date());
        popularSearchDao.insertData(popularSearch);
    }

    public List<PopularSearchVo> getAllPopular(){
        return popularSearchDao.getList();
    }
}
