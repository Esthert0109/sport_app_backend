package com.maindark.livestream.service;

import com.maindark.livestream.dao.LiveStreamMessageDao;
import com.maindark.livestream.domain.LiveStreamMessage;
import com.maindark.livestream.enums.StatusEnum;
import com.maindark.livestream.repository.LiveStreamMessageRepository;
import jakarta.annotation.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class LiveStreamMessageService {
    @Resource
    LiveStreamMessageRepository repository;

    @Resource
    LiveStreamMessageDao liveStreamMessageDao;


    public Page<LiveStreamMessage> findAll(Pageable pageable) {
        //        repository.findAll(pageable);
        return repository.findAllByAndStatus(StatusEnum.UP.getCode(),pageable);

    }
    public LiveStreamMessage getById(Integer id){
        return liveStreamMessageDao.getById(id);
    }
}
