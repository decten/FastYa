package com.developer.maker.dmaker.repository;

import com.developer.maker.dmaker.entity.Developer;
import com.developer.maker.dmaker.entity.RetiredDeveloper;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RetiredDeveloperRepository
    extends JpaRepository<RetiredDeveloper, Long> {

}
