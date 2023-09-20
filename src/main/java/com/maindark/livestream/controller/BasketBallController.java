package com.maindark.livestream.controller;

import com.maindark.livestream.service.BasketBallService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/basketballs")
public class BasketBallController {

    @Resource
    BasketBallService basketBallService;
}
