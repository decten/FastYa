package com.developer.maker.dmaker.controller;

import com.developer.maker.dmaker.service.DMakerService;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class DMakerController {
    private final DMakerService dMakerService;

    //GET /developers 요청 처리
    @GetMapping("/developers")
    public List<String> getAllDevelopers(){
        log.info("GET /developers HTTP/1.1");

        return Arrays.asList("snow", "Elsa", "Anna");
    }

    //Get은 데이터를 받아오는 것으로 지금처럼 생성은 post가 맞음
    @GetMapping("/create-developers")
    public List<String> createDevelopers(){
        log.info("GET /create-developers HTTP/1.1");

        dMakerService.createDeveloper();

        return Collections.singletonList("Barbie");
    }

}
