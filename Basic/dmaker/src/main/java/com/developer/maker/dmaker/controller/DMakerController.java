package com.developer.maker.dmaker.controller;

import com.developer.maker.dmaker.dto.CreateDeveloper;
import com.developer.maker.dmaker.dto.DeveloperDetailDto;
import com.developer.maker.dmaker.dto.DeveloperDto;
import com.developer.maker.dmaker.service.DMakerService;
import jakarta.validation.Valid;
import java.util.Arrays;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
public class DMakerController {
    private final DMakerService dMakerService;

    //GET /developers 요청 처리
    @GetMapping("/developers")
    public List<DeveloperDto> getAllDevelopers(){
        log.info("GET /developers HTTP/1.1");

        return dMakerService.getAllDevelopers();
    }

    @GetMapping("/developer/{memberId}")
    public DeveloperDetailDto getDeveloperDetail(
            @PathVariable String memberId
    ){
        log.info("GET /developers HTTP/1.1");

        return dMakerService.getDeveloperDetail(memberId);
    }

    @PostMapping("/create-developers")
    public CreateDeveloper.Response createDevelopers(
        @Valid @RequestBody CreateDeveloper.Request request
    ){
        log.info("request : {}", request);

        return dMakerService.createDeveloper(request);
    }

}
