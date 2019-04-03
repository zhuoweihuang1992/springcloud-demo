package com.alog.dataservice.auth.repository;

import com.alog.dataservice.auth.entity.AuthResource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


public interface RoleResourceRepository extends JpaRepository<AuthResource,Integer> {
    @Query("select a from AuthResource a where a.username = ?1 and a.role = ?2 and a.resource = ?3")
    AuthResource findByUsername(String username, String role, String resource);
}
