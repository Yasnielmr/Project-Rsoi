package com.rsoi.kursach.repository;

import com.rsoi.kursach.dto.UserFilterDto;
import com.rsoi.kursach.dto.UserViewDto;
import java.util.List;

public interface UserJdbcRepository{ 
        
    long searchPageableTotalCount(UserFilterDto filter);
    List<UserViewDto> searchPageable(UserFilterDto filter);
    boolean validCurrentPassword(String username, String password);
    
}
