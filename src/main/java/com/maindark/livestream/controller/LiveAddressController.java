package com.maindark.livestream.controller;

import com.maindark.livestream.domain.feijing.FeiJingLiveAddress;
import com.maindark.livestream.result.Result;
import com.maindark.livestream.service.FeiJingLiveAddressService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/live-address")
@CrossOrigin
public class LiveAddressController {

    @Resource
    FeiJingLiveAddressService feiJingLiveAddressService;

    @GetMapping("/{sportType}/{matchId}")
    public Result<FeiJingLiveAddress> getLiveAddressByMatchId(@PathVariable("sportType")String sportType,@PathVariable("matchId")String matchId){
        FeiJingLiveAddress feiJingLiveAddress = feiJingLiveAddressService.getAddressByMatchId(Integer.parseInt(sportType),Integer.parseInt(matchId));
        return Result.success(feiJingLiveAddress);
    }
}
