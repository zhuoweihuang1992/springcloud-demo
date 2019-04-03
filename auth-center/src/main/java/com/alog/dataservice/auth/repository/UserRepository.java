package com.alog.dataservice.auth.repository;

import com.alog.dataservice.auth.entity.User;
import org.springframework.data.repository.CrudRepository;


public interface UserRepository extends CrudRepository<User,Integer> {
    User findByUsername(String username);
}
