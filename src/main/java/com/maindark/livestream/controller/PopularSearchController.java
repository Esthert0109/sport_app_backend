package com.maindark.livestream.controller;

import com.maindark.livestream.form.PopularSearchForm;
import com.maindark.livestream.result.Result;
import com.maindark.livestream.service.PopularSearchService;
import com.maindark.livestream.vo.PopularSearchVo;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/populars")
public class PopularSearchController {
    @Resource
    PopularSearchService popularSearchService;

    @PostMapping("/")
    public Result<Boolean> createPopular(@RequestBody @Valid PopularSearchForm popularSearchForm){
        popularSearchService.createPopularSearch(popularSearchForm);
        return Result.success(true);
    }

    @GetMapping("/list")
    public Result<List<PopularSearchVo>> getAllPopularData(){
        List<PopularSearchVo> popularSearchVos = popularSearchService.getAllPopular();
        return Result.success(popularSearchVos);
    }
}
