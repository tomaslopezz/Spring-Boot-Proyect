package com.demoproject.bump.repositories;

import com.demoproject.bump.entities.UserEntity;
import org.springframework.data.domain.Example;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Long> {

    Optional<UserEntity> findByMail(String mail);
}
