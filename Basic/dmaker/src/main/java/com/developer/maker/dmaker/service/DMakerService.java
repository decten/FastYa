package com.developer.maker.dmaker.service;

import com.developer.maker.dmaker.dto.CreateDeveloper;
import com.developer.maker.dmaker.dto.CreateDeveloper.Request;
import com.developer.maker.dmaker.entity.Developer;
import com.developer.maker.dmaker.repository.DeveloperRepository;
import com.developer.maker.dmaker.type.DeveloperLevel;
import com.developer.maker.dmaker.type.DeveloperSkillType;
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
        Developer developer = Developer.builder()
            .developerLevel(DeveloperLevel.JUNIOR)
            .developerSkillType(DeveloperSkillType.BACK_END)
            .experienceYears(2)
            .name("Barbie")
            .age(27)
            .build();

        developerRepository.save(developer);
    }
}
