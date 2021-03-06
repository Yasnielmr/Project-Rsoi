package com.rsoi.kursach.repository;

import com.rsoi.kursach.dto.UserDto;
import java.util.List;

public interface UserRepository {
    
     UserDto findByUserName(String userName);

    List<UserDto> findUsersByName(String name) ;
       
}
