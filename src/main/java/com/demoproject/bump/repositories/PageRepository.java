package com.demoproject.bump.repositories;

import com.demoproject.bump.entities.PageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface PageRepository extends JpaRepository<PageEntity, Long> {

    Optional<PageEntity> findByTitle(String title);

    boolean existsBytitle(String title);

    @Modifying
    @Query("DELETE FROM PageEntity WHERE title=:title")
    void deleteByTitle(String title);
}
