package com.mamdy.dao;

import com.mamdy.entities.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface UserDaoRepository extends JpaRepository<AppUser, Long> {
    AppUser findByUsername(String username);
}
