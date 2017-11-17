package com.rsoi.kursach.repository;

import com.rsoi.kursach.models.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RestResource;

@RestResource(path = "users", rel = "user")
public interface UserRepository extends CrudRepository<User, Long>{
       
}
