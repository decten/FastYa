package com.developer.maker.dmaker.service;

import static com.developer.maker.dmaker.code.StatusCode.EMPLOYED;
import static com.developer.maker.dmaker.type.DeveloperLevel.SENIOR;
import static com.developer.maker.dmaker.type.DeveloperSkillType.FRONT_END;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.developer.maker.dmaker.dto.CreateDeveloper;
import com.developer.maker.dmaker.dto.DeveloperDetailDto;
import com.developer.maker.dmaker.entity.Developer;
import com.developer.maker.dmaker.exception.DMakerErrorCode;
import com.developer.maker.dmaker.exception.DMakerException;
import com.developer.maker.dmaker.repository.DeveloperRepository;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class DMakerServiceTest {

    @Mock
    private DeveloperRepository developerRepository;
    @InjectMocks
    private DMakerService dMakerService;

    private final Developer defaultDeveloper = Developer.builder()
        .developerLevel(SENIOR)
        .developerSkillType(FRONT_END)
        .experienceYears(12)
        .statusCode(EMPLOYED)
        .name("name")
        .age(32)
        .build();

    private final CreateDeveloper.Request defaultCreateRequest =
        CreateDeveloper.Request.builder()
            .developerLevel(SENIOR)
            .developerSkillType(FRONT_END)
            .experienceYears(12)
            .memberId("memberId")
            .name("name")
            .age(32)
            .build();

    @Test
    void readDeveloperTest() {
        //given
        given(developerRepository.findByMemberId(anyString()))
            .willReturn(Optional.of(defaultDeveloper));

        //when
        DeveloperDetailDto developerDetail = dMakerService.getDeveloperDetail("memberId");

        //then
        assertEquals(SENIOR, developerDetail.getDeveloperLevel());
        assertEquals(FRONT_END, developerDetail.getDeveloperSkillType());
        assertEquals(12, developerDetail.getExperienceYears());
    }

    @Test
    void createDeveloperTest_success() {
        //given
        given(developerRepository.findByMemberId(anyString()))
            .willReturn(Optional.empty()); //아무것도 return이 안 돼야 잘 만들어진 것

        ArgumentCaptor<Developer> captor =
            ArgumentCaptor.forClass(Developer.class);

        //when
        dMakerService.createDeveloper(defaultCreateRequest);

        //then
        verify(developerRepository, times(1))
            .save(captor.capture());
        Developer savedDeveloper = captor.getValue();
        assertEquals(SENIOR, savedDeveloper.getDeveloperLevel());
        assertEquals(FRONT_END, savedDeveloper.getDeveloperSkillType());
        assertEquals(12, savedDeveloper.getExperienceYears());
    }

    @Test
    void createDeveloperTest_failed_with_duplicated() {
        //given
        given(developerRepository.findByMemberId(anyString()))
            .willReturn(Optional.of(defaultDeveloper));

        //when
        //then
        /* 실행과 검증이 동시에 이뤄져서 when&then */
        DMakerException dMakerException = assertThrows(DMakerException.class,
            () -> dMakerService.createDeveloper(defaultCreateRequest)
        );
        assertEquals(DMakerErrorCode.DUPLICATED_MEMBER_ID, dMakerException.getDMakerErrorCode());
    }

}