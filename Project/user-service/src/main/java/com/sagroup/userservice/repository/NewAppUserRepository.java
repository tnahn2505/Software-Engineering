package com.sagroup.userservice.repository;

import com.sagroup.userservice.entity.NewAppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface NewAppUserRepository extends JpaRepository<NewAppUser, Long> {
    NewAppUser findByUsernameIgnoreCase(String username);
    NewAppUser findByResetToken(String token);
    Optional<NewAppUser> findByUsername(String username);
    boolean existsByUsernameIgnoreCase(String username);
}

