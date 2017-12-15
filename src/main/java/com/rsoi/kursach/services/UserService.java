package com.rsoi.kursach.services;

import com.rsoi.kursach.dto.PageableResult;
import com.rsoi.kursach.dto.UserDto;
import com.rsoi.kursach.dto.UserFilterDto;
import com.rsoi.kursach.dto.UserViewDto;
import java.util.List;

public interface UserService {
    
    PageableResult find(UserFilterDto filter);
    List<UserViewDto> searchUser(UserFilterDto value);
    List<UserViewDto> searchManager(String query);
    int inactive(String username);
    int locked(String username);
    UserDto loadUserByUsername(String username);

    UserDto findById(long userId);
    Long mergeUser(UserDto value);
    String deleteUser(long userId);

    UserDto findByUsername(String userName);
    Long updateProfile(UserDto value);
    
}
