package com.developer.maker.dmaker.service;

import static exception.DMakerErrorCode.LEVEL_EXPERIENCE_YEARS_NOT_MATCHED;

import com.developer.maker.dmaker.dto.CreateDeveloper;
import com.developer.maker.dmaker.dto.CreateDeveloper.Request;
import com.developer.maker.dmaker.entity.Developer;
import com.developer.maker.dmaker.repository.DeveloperRepository;
import com.developer.maker.dmaker.type.DeveloperLevel;
import com.developer.maker.dmaker.type.DeveloperSkillType;
import exception.DMakerErrorCode;
import exception.DMakerException;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DMakerService {
    private final DeveloperRepository developerRepository;

    @Transactional
    public void createDeveloper(CreateDeveloper.Request request){
        validateCreateDeveloperRequest(request);

        //비즈니스 로직 수행
        Developer developer = Developer.builder()
            .developerLevel(DeveloperLevel.JUNIOR)
            .developerSkillType(DeveloperSkillType.BACK_END)
            .experienceYears(2)
            .name("Barbie")
            .age(27)
            .build();

        developerRepository.save(developer);
    }

    private void validateCreateDeveloperRequest(Request request) {
        //비즈니스 밸리데이션 수행
        if(request.getDeveloperLevel() == DeveloperLevel.SENIOR
        && request.getExperienceYears() < 10){
            throw new DMakerException(LEVEL_EXPERIENCE_YEARS_NOT_MATCHED);
        }
    }
}
