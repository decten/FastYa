package com.developer.maker.dmaker.controller;

import com.developer.maker.dmaker.dto.CreateDeveloper;
import com.developer.maker.dmaker.dto.DeveloperDetailDto;
import com.developer.maker.dmaker.dto.DeveloperDto;
import com.developer.maker.dmaker.dto.EditDeveloper;
import com.developer.maker.dmaker.service.DMakerService;
import jakarta.validation.Valid;
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

        return dMakerService.getAllEmployedDevelopers();
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
    @PutMapping("/developer/{memberId}")
    public DeveloperDetailDto editDeveloper(
        @PathVariable String memberId,
        @Valid @RequestBody EditDeveloper.Request request
    ){
        log.info("GET /developers HTTP/1.1");

        return dMakerService.editDeveloper(memberId, request);
    }

    @DeleteMapping("/developer/{memberId}")
    public DeveloperDetailDto deleteDeveloper(
        @PathVariable String memberId
    ){
        return dMakerService.deleteDeveloper(memberId);
    }
}
