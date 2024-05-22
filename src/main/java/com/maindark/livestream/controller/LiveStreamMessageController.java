package com.maindark.livestream.controller;

import com.maindark.livestream.domain.LiveStreamMessage;
import com.maindark.livestream.result.Result;
import com.maindark.livestream.service.LiveStreamMessageService;
import jakarta.annotation.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/v1/messages/")
@CrossOrigin
public class LiveStreamMessageController {

    @Resource
    LiveStreamMessageService messageService;

    @GetMapping("/list")
    public Result<Page<LiveStreamMessage>> list(@RequestParam(value = "page", defaultValue = "1") Integer page,
                                                @RequestParam(value = "size", defaultValue = "10") Integer size
                                               ) {
        PageRequest request = PageRequest.of(page - 1, size, Sort.Direction.DESC,"createDate");
        Page<LiveStreamMessage> liveStreamMessagesPage = messageService.findAll(request);
        return Result.success(liveStreamMessagesPage);
    }

    @GetMapping("/{id}")
    public Result<LiveStreamMessage> getById(@PathVariable Integer id){
        LiveStreamMessage liveStreamMessage = messageService.getById(id);
        return Result.success(liveStreamMessage);
    }

}
