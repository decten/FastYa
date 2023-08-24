package com.developer.maker.dmaker.service;

import com.developer.maker.dmaker.code.StatusCode;
import com.developer.maker.dmaker.dto.CreateDeveloper;
import com.developer.maker.dmaker.dto.CreateDeveloper.Request;
import com.developer.maker.dmaker.dto.DeveloperDetailDto;
import com.developer.maker.dmaker.dto.DeveloperDto;
import com.developer.maker.dmaker.dto.EditDeveloper;
import com.developer.maker.dmaker.entity.Developer;
import com.developer.maker.dmaker.entity.RetiredDeveloper;
import com.developer.maker.dmaker.repository.DeveloperRepository;
import com.developer.maker.dmaker.repository.RetiredDeveloperRepository;
import com.developer.maker.dmaker.type.DeveloperLevel;
import com.developer.maker.dmaker.exception.DMakerException;
import lombok.NonNull;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static com.developer.maker.dmaker.exception.DMakerErrorCode.*;

@Service
@RequiredArgsConstructor
public class DMakerService {
    private final DeveloperRepository developerRepository;
    private final RetiredDeveloperRepository retiredDeveloperRepository;

    @Transactional
    public CreateDeveloper.Response createDeveloper(Request request) {
        validateCreateDeveloperRequest(request);

        //비즈니스 로직 수행
        return CreateDeveloper.Response.fromEntity(
            developerRepository.save(createDeveloperFromRequest(request))
        );
    }

    private Developer createDeveloperFromRequest(Request request) {

        return Developer.builder()
            .developerLevel(request.getDeveloperLevel())
            .developerSkillType(request.getDeveloperSkillType())
            .experienceYears(request.getExperienceYears())
            .memberId(request.getMemberId())
            .statusCode(StatusCode.EMPLOYED)
            .name(request.getName())
            .age(request.getAge())
            .build();

    }

    private void validateCreateDeveloperRequest(@NonNull Request request) {
        if (request == null) {
            throw new DMakerException(INVALID_REQUEST);
        }
        //비즈니스 밸리데이션 수행
        validateDeveloperLevel(
            request.getDeveloperLevel(),
            request.getExperienceYears()
        );

        developerRepository.findByMemberId(request.getMemberId())
            .ifPresent(developer->{
                throw new DMakerException(DUPLICATED_MEMBER_ID);
            });

    }

    @Transactional(readOnly = true)
    public List<DeveloperDto> getAllEmployedDevelopers() {
        return developerRepository.findDeveloperByStatusCodeEquals(StatusCode.EMPLOYED)
                .stream().map(DeveloperDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public DeveloperDetailDto getDeveloperDetail(String memberId) {
        return developerRepository.findByMemberId(memberId)
                .map(DeveloperDetailDto::fromEntity)
                .orElseThrow(()->new DMakerException(NO_DEVELOPER));
    }
    @Transactional     //DB에도 반영
    public DeveloperDetailDto editDeveloper(String memberId, EditDeveloper.Request request){
        validateDeveloperLevel(request.getDeveloperLevel(),request.getExperienceYears());

        Developer developer = developerRepository.findByMemberId(memberId).orElseThrow(
            ()->new DMakerException(NO_DEVELOPER)
        );

        developer.setDeveloperLevel(request.getDeveloperLevel());
        developer.setDeveloperSkillType(request.getDeveloperSkillType());
        developer.setExperienceYears(request.getExperienceYears());

        return DeveloperDetailDto.fromEntity(developer);
    }

    private static void validateDeveloperLevel(DeveloperLevel developerLevel,
        Integer experienceYears) {
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
    }
    @Transactional
    public DeveloperDetailDto deleteDeveloper(String memberId) {
        // 1. EMPLOYED -> RETIRED
        Developer developer = developerRepository.findByMemberId(memberId)
            .orElseThrow(()-> new DMakerException(NO_DEVELOPER));
        developer.setStatusCode(StatusCode.RETIRED);

        // 2. save into RetireDeveloper
        RetiredDeveloper retiredDeveloper = RetiredDeveloper.builder()
            .memberId(memberId)
            .name(developer.getName())
            .build();
        retiredDeveloperRepository.save(retiredDeveloper);

        return DeveloperDetailDto.fromEntity(developer);
    }
}
