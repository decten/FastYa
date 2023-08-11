package com.developer.maker.dmaker.repository;

import com.developer.maker.dmaker.entity.Developer;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DeveloperRepository
    extends JpaRepository<Developer, Long> {

    Optional<Developer> findByMemberId(String memberId);
}
