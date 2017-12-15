package com.rsoi.kursach.rest;

import com.rsoi.kursach.dto.*;
import com.rsoi.kursach.repository.*;
import com.rsoi.kursach.services.UserService;
import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {
        
    @Autowired
    private UsersCustomRepository usersCustomRepository;

    @Autowired
    UserService userService;

    @Autowired
    UserJpaRepository userJpaRepository;

    @Autowired
    UserRolJpaRepository userRolJpaRepository;

   @Autowired
   RoleJdbcRepository roleJdbcRepository;
   
   @RequestMapping(value = "/search", method = RequestMethod.POST, produces = "application/json")
    public PageableResult searchUsers(@RequestBody UserFilterDto filter){
        return userService.find(filter);
    }

    @RequestMapping(value = "/find/{id}", method = RequestMethod.GET, produces = "application/json")
    public UserDto viewUser(@PathVariable("id")Long userId){
        UserDto dto= userService.findById(userId);
         return dto;
    }

    @RequestMapping(value = "/findProfile/{username}", method = RequestMethod.GET, produces = "application/json")
    public UserDto profileUser(@PathVariable("username")String userName){
        UserDto dto= userService.findByUsername(userName);
        return dto;
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST)
	public ResponseEntity<Long> createUser(@RequestBody UserDto value) {
		Long id = userService.mergeUser(value);
		return new ResponseEntity<Long>(id, HttpStatus.CREATED);
	}


    @RequestMapping(value = "/delete/", method = RequestMethod.POST)
	public ResponseEntity<String> deleteUser(@RequestBody UserDto value) {
		String message = userService.deleteUser(value.getId());
		return new ResponseEntity<String>("{\"message\":\"" + message + "\"}", HttpStatus.CREATED);
	}

    @RequestMapping(value = "/updateProfile", method = RequestMethod.POST)
    public ResponseEntity<Long> updateProfile(@RequestBody UserDto value) {
        Long id = userService.updateProfile(value);
        return new ResponseEntity<Long>(id, HttpStatus.CREATED);
    }


    @RequestMapping(value = "/findByName", method = RequestMethod.GET, produces = "application/json")
    public UserDto findByName(@RequestParam String username) {
        return userService.loadUserByUsername(username);
    }

    @RequestMapping(value = "/findUsersByName", method = RequestMethod.GET, produces = "application/json")
    public List<UserDto> findUsersByName(String name) {
        List<UserDto> items = new ArrayList<>();
        return usersCustomRepository.findUsersByName(name);
    }

    @RequestMapping(value = "/findRoles", method = RequestMethod.GET, produces = "application/json")
    public List<RoleViewDto> findAllRoles(){
         return roleJdbcRepository.searchRoleList();
    }

    /*
    @RequestMapping(value = "/inactive", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public Map inactiveByUsername(@RequestBody String userName) {
        userService.inactive(userName);
        return SUCCESS_MSG(String.format("User %s was inactive", userName));
    }

    @RequestMapping(value = "/locked", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public Map lockedByUsername(@RequestBody String userName) {
        userService.locked(userName);
        return SUCCESS_MSG(String.format("User %s was locked", userName));
    }*/
    
}
