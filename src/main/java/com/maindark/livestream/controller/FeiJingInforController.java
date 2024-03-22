package com.maindark.livestream.controller;

import com.maindark.livestream.domain.feijing.FeiJingInfor;
import com.maindark.livestream.domain.feijing.InfoCategory;
import com.maindark.livestream.result.Result;
import com.maindark.livestream.service.FeiJingInfoService;
import jakarta.annotation.Resource;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/info")
public class FeiJingInforController {

    @Resource
    FeiJingInfoService feiJingInfoService;
    @GetMapping("/list")
    public Result<List<FeiJingInfor>> getInfoList(
                                                  @RequestParam(value = "page", defaultValue = "1") Integer page,
                                                  @RequestParam(value = "size", defaultValue = "10") Integer size,
                                                  @RequestParam(required = false) String search){
        PageRequest request = PageRequest.of(page - 1, size, Sort.Direction.DESC,"created_date");
        List<FeiJingInfor> list = feiJingInfoService.getInfoList(search,request);
        return Result.success(list);
    }

    @GetMapping("/categories")
    public Result<List<InfoCategory>> getCategories(){
        List<InfoCategory> list = feiJingInfoService.getCategories();
        return Result.success(list);
    }
}
