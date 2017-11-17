package com.rsoi.kursach.models;

import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class User {
    
    @Id
    @GeneratedValue
    private Long id;
    
    private String username;
    
    private String email;
    
    private Integer age;
    private String address;

    public User() {
        super();
    }

    public User(Long id, String username, String email, Integer age, String address) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.age = age;
        this.address = address;
    }
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

   
    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
    
   
}
