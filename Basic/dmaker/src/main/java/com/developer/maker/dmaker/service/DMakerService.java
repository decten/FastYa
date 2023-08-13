package com.developer.maker.dmaker.service;

import com.developer.maker.dmaker.dto.CreateDeveloper;
import com.developer.maker.dmaker.dto.CreateDeveloper.Request;
import com.developer.maker.dmaker.dto.DeveloperDetailDto;
import com.developer.maker.dmaker.dto.DeveloperDto;
import com.developer.maker.dmaker.entity.Developer;
import com.developer.maker.dmaker.repository.DeveloperRepository;
import com.developer.maker.dmaker.type.DeveloperLevel;
import exception.DMakerException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static exception.DMakerErrorCode.*;

@Service
@RequiredArgsConstructor
public class DMakerService {
    private final DeveloperRepository developerRepository;

    @Transactional
    public CreateDeveloper.Response createDeveloper(Request request){
        validateCreateDeveloperRequest(request);

        //비즈니스 로직 수행
        Developer developer = Developer.builder()
            .developerLevel(request.getDeveloperLevel())
            .developerSkillType(request.getDeveloperSkillType())
            .experienceYears(request.getExperienceYears())
            .memberId(request.getMemberId())
            .name(request.getName())
            .age(request.getAge())
            .build();

        developerRepository.save(developer);
        return CreateDeveloper.Response.fromEntity(developer);
    }

    private void validateCreateDeveloperRequest(Request request) {
        //비즈니스 밸리데이션 수행
        DeveloperLevel developerLevel = request.getDeveloperLevel();
        Integer experienceYears = request.getExperienceYears();
        if(developerLevel == DeveloperLevel.SENIOR
            && experienceYears < 10){
            throw new DMakerException(LEVEL_EXPERIENCE_YEARS_NOT_MATCHED);
        }
        if(developerLevel == DeveloperLevel.JUNGNIOR
            && (experienceYears < 4 || experienceYears > 10)){
            throw new DMakerException(LEVEL_EXPERIENCE_YEARS_NOT_MATCHED);
        }
        if(developerLevel == DeveloperLevel.JUNIOR
            && experienceYears > 4){
            throw new DMakerException(LEVEL_EXPERIENCE_YEARS_NOT_MATCHED);
        }

        developerRepository.findByMemberId(request.getMemberId())
            .ifPresent(developer->{
                throw new DMakerException(DUPLICATED_MEMBER_ID);
            });

    }

    public List<DeveloperDto> getAllDevelopers() {
        return developerRepository.findAll()
                .stream().map(DeveloperDto::fromEntity)
                .collect(Collectors.toList());
    }

    public DeveloperDetailDto getDeveloperDetail(String memberId) {
        return developerRepository.findByMemberId(memberId)
                .map(DeveloperDetailDto::fromEntity)
                .orElseThrow(()->new DMakerException(NO_DEVELOPER));
    }
}
