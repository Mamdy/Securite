package com.mamdy.dao;

import com.mamdy.entities.AppRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface RolesDaoRepositrory extends JpaRepository<AppRole, Long> {
    public AppRole findByRoleName(String roleName);
}
