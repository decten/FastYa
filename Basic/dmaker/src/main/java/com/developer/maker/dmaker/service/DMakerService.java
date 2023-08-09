package com.developer.maker.dmaker.service;

import com.developer.maker.dmaker.entity.Developer;
import com.developer.maker.dmaker.repository.DeveloperRepository;
import com.developer.maker.dmaker.type.DeveloperLevel;
import com.developer.maker.dmaker.type.DeveloperSkillType;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DMakerService {
    private final DeveloperRepository developerRepository;

    @Transactional
    public void createDeveloper(){
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
