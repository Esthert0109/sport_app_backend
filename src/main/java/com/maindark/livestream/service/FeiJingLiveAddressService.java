package com.maindark.livestream.service;

import com.maindark.livestream.dao.FeiJingLiveAddressDao;
import com.maindark.livestream.domain.feijing.FeiJingLiveAddress;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

@Service
public class FeiJingLiveAddressService {

    @Resource
    FeiJingLiveAddressDao feiJingLiveAddressDao;


    public FeiJingLiveAddress getAddressByMatchId(Integer sportType,Integer matchId){
        return feiJingLiveAddressDao.getAddressByMatchId(sportType,matchId);
    }
}
