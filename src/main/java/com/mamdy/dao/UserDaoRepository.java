package com.mamdy.dao;

import com.mamdy.entities.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.Optional;

@RepositoryRestResource
public interface UserDaoRepository extends JpaRepository<AppUser, Long> {
    AppUser findByEmail(final String email);
    Optional<AppUser> findById(final Long id);
}
