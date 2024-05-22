package com.maindark.livestream.controller;

import com.maindark.livestream.domain.LiveStreamUser;
import com.maindark.livestream.domain.feijing.FeiJingInfor;
import com.maindark.livestream.domain.feijing.InfoCategory;
import com.maindark.livestream.result.Result;
import com.maindark.livestream.service.FeiJingInfoService;
import com.maindark.livestream.vo.FeiJingInfoVo;
import jakarta.annotation.Resource;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/info")
@CrossOrigin
public class FeiJingInforController {

    @Resource
    FeiJingInfoService feiJingInfoService;
    @GetMapping("/list")
    public Result<List<FeiJingInfoVo>> getInfoList(
                                                  @RequestParam(value = "page", defaultValue = "1") Integer page,
                                                  @RequestParam(value = "size", defaultValue = "10") Integer size,
                                                  @RequestParam(required = false) String search){
        PageRequest request = PageRequest.of(page - 1, size, Sort.Direction.DESC,"created_date");
        List<FeiJingInfoVo> list = feiJingInfoService.getInfoList(search,request);
        return Result.success(list);
    }

    @GetMapping("/categories")
    public Result<List<InfoCategory>> getCategories(){
        List<InfoCategory> list = feiJingInfoService.getCategories();
        return Result.success(list);
    }

    @GetMapping("/{id}")
    public Result<FeiJingInfor> getInfoById(LiveStreamUser liveStreamUser, @PathVariable("id")Integer id){
        Long userId = null;
        if(liveStreamUser != null) {
            userId = liveStreamUser.getId();
        }
        FeiJingInfor feiJingInfor = feiJingInfoService.getInfoById(id,userId);
        return Result.success(feiJingInfor);
    }

    @GetMapping("/popular/{categoryId}")
    public Result<List<FeiJingInfoVo>> getPopularList(
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam(value = "size", defaultValue = "10") Integer size,
            @PathVariable("categoryId") Integer categoryId){
        PageRequest request = PageRequest.of(page - 1, size, Sort.Direction.DESC,"created_date");
        List<FeiJingInfoVo> list = feiJingInfoService.getPopularList(categoryId,request);
        return Result.success(list);
    }

    @GetMapping("/list/isTop")
    public Result<List<FeiJingInfoVo>> getInfoTopList(
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam(value = "size", defaultValue = "10") Integer size,
            @RequestParam(required = false) String search){
        PageRequest request = PageRequest.of(page - 1, size, Sort.Direction.DESC,"created_date");
        List<FeiJingInfoVo> list = feiJingInfoService.getInfoTopList(search,request);
        return Result.success(list);
    }

    @GetMapping("/list/urls/{search}")
    public Result<List<FeiJingInfoVo>> getInfoUrls(
            @PathVariable("search") Integer search){
        List<FeiJingInfoVo> list = feiJingInfoService.getInfoUrls(search);
        return Result.success(list);
    }

}
