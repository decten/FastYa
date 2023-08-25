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

import static com.developer.maker.dmaker.code.DMakerErrorCode.*;

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
        request.getDeveloperLevel().validateExperienceYears(
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
        return DeveloperDetailDto.fromEntity(getDeveloperByMemberId(memberId));
    }

    private Developer getDeveloperByMemberId(String memberId){
        return developerRepository.findByMemberId(memberId)
            .orElseThrow(()->new DMakerException(NO_DEVELOPER));
    }

    @Transactional     //DB에도 반영
    public DeveloperDetailDto editDeveloper(String memberId, EditDeveloper.Request request){

        request.getDeveloperLevel().validateExperienceYears(
                request.getExperienceYears()
        );

        return DeveloperDetailDto.fromEntity(
                getUpdatedDeveloperFromRequest(
                    request, getDeveloperByMemberId(memberId)
            )
        );

    }

    private Developer getUpdatedDeveloperFromRequest(EditDeveloper.Request request, Developer developer) {
        developer.setDeveloperLevel(request.getDeveloperLevel());
        developer.setDeveloperSkillType(request.getDeveloperSkillType());
        developer.setExperienceYears(request.getExperienceYears());

        return developer;
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
