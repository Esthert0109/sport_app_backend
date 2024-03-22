package com.maindark.livestream.controller;

import com.maindark.livestream.form.LiveStreamDetailForm;
import com.maindark.livestream.result.Result;
import com.maindark.livestream.service.PushAndPlayService;
import com.maindark.livestream.vo.LiveStreamDetailVo;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/pushAndPlay")
public class PushAndPlayController {

    @Resource
    PushAndPlayService pushAndPlayService;

    @GetMapping("/pushUrl")
    public Result<Map<String,Object>> getPushUrl(){
        Map<String,Object> map = pushAndPlayService.getPushUrl();
        return Result.success(map) ;
    }

    @PostMapping("/create/{userId}")
    public Result<Integer> createLiveStreamDetail(@PathVariable("userId")String userId, @Valid @RequestBody LiveStreamDetailForm liveStreamDetailForm){
        Integer result = pushAndPlayService.createLiveStream(userId,liveStreamDetailForm);
        return Result.success(result);
    }

    @GetMapping("/{id}")
    public Result<LiveStreamDetailVo> getLiveStreamDetailById(@PathVariable("id")Integer id){
        LiveStreamDetailVo liveStreamDetailVo = pushAndPlayService.getLiveStreamDetailById(id);
        return Result.success(liveStreamDetailVo);
    }

    @GetMapping("/details/{userId}")
    public Result<LiveStreamDetailVo> getLiveStreamDetailByUserId(@PathVariable("userId") Long userId) {
        LiveStreamDetailVo liveStreamDetailVo = pushAndPlayService.getLiveStreamDetailsByUserId(userId);
        return Result.success(liveStreamDetailVo);
    }

    @PatchMapping("/{id}")
    public Result<Boolean> updateLiveStreamDetailById(@PathVariable("id")Integer id,@Valid @RequestBody LiveStreamDetailForm liveStreamDetailForm){
        pushAndPlayService.updateLiveStreamDetailById(id,liveStreamDetailForm);
        return Result.success(true);
    }

    @GetMapping("/list/popular")
    public Result<List<LiveStreamDetailVo>> getAllPopularLiveStreamDetails(@RequestParam(value = "page", defaultValue = "1") Integer page,
                                                                           @RequestParam(value = "size", defaultValue = "10") Integer size){
        PageRequest request = PageRequest.of(page - 1, size, Sort.Direction.DESC,"live_date");

        List<LiveStreamDetailVo> list = pushAndPlayService.getAllPopularLiveStreamDetails(request);
        return Result.success(list);
    }

    @GetMapping("/list")
    public Result<List<LiveStreamDetailVo>> getAllLiveStreamDetail(@RequestParam(value = "page", defaultValue = "1") Integer page,
                                                                   @RequestParam(value = "size", defaultValue = "10") Integer size){
        PageRequest request = PageRequest.of(page - 1, size, Sort.Direction.DESC,"live_date");
        List<LiveStreamDetailVo> list = pushAndPlayService.getAllLiveStreamDetails(request);
        return Result.success(list);
    }

    @GetMapping("/list/{sportType}")
    public Result<List<LiveStreamDetailVo>> getAllLiveStreamDetailBySportType(@PathVariable("sportType")String sportType,@RequestParam(value = "page", defaultValue = "1") Integer page,
                                                                   @RequestParam(value = "size", defaultValue = "10") Integer size){
        PageRequest request = PageRequest.of(page - 1, size, Sort.Direction.DESC,"live_date");
        List<LiveStreamDetailVo> list = pushAndPlayService.getAllLiveStreamDetailsBySportType(request,sportType);
        return Result.success(list);
    }

    @DeleteMapping("/{id}")
    public Result<Boolean> deleteLiveRoom(@PathVariable("id")Integer id){
        pushAndPlayService.deleteLiveRoomById(id);
        return Result.success(true);
    }




}
