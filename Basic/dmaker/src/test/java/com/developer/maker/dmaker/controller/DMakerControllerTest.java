package com.developer.maker.dmaker.controller;

import static org.hamcrest.CoreMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.developer.maker.dmaker.dto.DeveloperDto;
import com.developer.maker.dmaker.service.DMakerService;
import com.developer.maker.dmaker.type.DeveloperLevel;
import com.developer.maker.dmaker.type.DeveloperSkillType;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(DMakerController.class)
class DMakerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DMakerService dMakerService;

    protected MediaType contentType =
        new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(),
            StandardCharsets.UTF_8);

    @Test
    void getAllDevelopers() throws Exception {
        DeveloperDto juniorDeveloperDto = DeveloperDto.builder()
            .developerSkillType(DeveloperSkillType.BACK_END)
            .developerLevel(DeveloperLevel.JUNIOR)
            .memberId("memberId1")
            .build();

        DeveloperDto seniorDeveloperDto = DeveloperDto.builder()
            .developerSkillType(DeveloperSkillType.FRONT_END)
            .developerLevel(DeveloperLevel.SENIOR)
            .memberId("memberId2")
            .build();

        given(dMakerService.getAllEmployedDevelopers())
            .willReturn(Arrays.asList(juniorDeveloperDto, seniorDeveloperDto));

        mockMvc.perform(get("/developers").contentType(contentType))
            .andExpect(status().isOk())
            .andExpect(
                jsonPath("$.[0].developerSkillType",
                    is(DeveloperSkillType.BACK_END.name()))
            )
            .andExpect(
                jsonPath("$.[0].developerLevel",
                    is(DeveloperLevel.JUNIOR.name()))
            )
            .andExpect(
                jsonPath("$.[1].developerSkillType",
                    is(DeveloperSkillType.FRONT_END.name()))
            )
            .andExpect(
                jsonPath("$.[1].developerLevel",
                    is(DeveloperLevel.SENIOR.name()))
            );
    }
}