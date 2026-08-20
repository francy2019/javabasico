package com.mycompany.socialnetwork.repository;

import com.mycompany.socialnetwork.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Long> {

    Optional<UserEntity> findByMail(String mail);
}
