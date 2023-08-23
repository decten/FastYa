package com.developer.maker.dmaker.controller;

import com.developer.maker.dmaker.dto.CreateDeveloper;
import com.developer.maker.dmaker.dto.DMakerErrorResponse;
import com.developer.maker.dmaker.dto.DeveloperDetailDto;
import com.developer.maker.dmaker.dto.DeveloperDto;
import com.developer.maker.dmaker.dto.EditDeveloper;
import com.developer.maker.dmaker.service.DMakerService;
import com.developer.maker.dmaker.exception.DMakerException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
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
            @PathVariable final String memberId
    ){
        log.info("GET /developers HTTP/1.1");

        return dMakerService.getDeveloperDetail(memberId);
    }

    @PostMapping("/create-developers")
    public CreateDeveloper.Response createDevelopers(
        @Valid @RequestBody final CreateDeveloper.Request request
    ){
        log.info("request : {}", request);

        return dMakerService.createDeveloper(request);
    }
    @PutMapping("/developer/{memberId}")
    public DeveloperDetailDto editDeveloper(
        @PathVariable final String memberId,
        @Valid @RequestBody final EditDeveloper.Request request
    ){
        log.info("GET /developers HTTP/1.1");

        return dMakerService.editDeveloper(memberId, request);
    }

    @DeleteMapping("/developer/{memberId}")
    public DeveloperDetailDto deleteDeveloper(
        @PathVariable final String memberId
    ){
        return dMakerService.deleteDeveloper(memberId);
    }

}
