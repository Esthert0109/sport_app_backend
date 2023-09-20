package com.maindark.livestream.controller;

import com.maindark.livestream.domain.LiveStreamUser;
import com.maindark.livestream.result.Result;
import com.maindark.livestream.service.LiveStreamCollectionService;
import com.maindark.livestream.vo.LiveStreamCollectionVo;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/collections")
public class LiveStreamCollectionController {

    @Resource
    LiveStreamCollectionService liveStreamCollectionService;

    @GetMapping("/list")
    public Result<List<LiveStreamCollectionVo>> getAllCollection(LiveStreamUser liveStreamUser) {
        Long id = liveStreamUser.getId();
        List<LiveStreamCollectionVo> liveStreamCollectionVos = liveStreamCollectionService.getAllCollectionByUserId(Math.toIntExact(id));
        return Result.success(liveStreamCollectionVos);
    }

    @GetMapping("/{competitionId}")
    public Result<Object> getCompetitionByCompetitionId(LiveStreamUser liveStreamUser,@PathVariable Integer competitionId){
        Object object = liveStreamCollectionService.getMatchById(competitionId);
        return Result.success(object);
    }

    @DeleteMapping("/{id}")
    public Result<Boolean> deleteCollectionById(LiveStreamUser liveStreamUser, @PathVariable Integer id){
        liveStreamCollectionService.deleteCollectionById(id);
        return Result.success(true);
    }


}
