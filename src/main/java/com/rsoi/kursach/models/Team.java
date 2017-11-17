package com.rsoi.kursach.models;

import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;

@Entity
public class Team {
    
    @Id
    @GeneratedValue    
    private Long id;
    private String name;
    private String location;
    private String mascotte;
    
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "teamId")
    private Set<User> user;

    public Team() {
        
        super();
    }

    public Team(Long id, String name, String location, String mascotte, Set<User> user) {
        this.id = id;
        this.name = name;
        this.location = location;
        this.mascotte = mascotte;
        this.user = user;
    }
    
    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getLocation() {
        return location;
    }

    public String getMascotte() {
        return mascotte;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setMascotte(String mascotte) {
        this.mascotte = mascotte;
    }

    public Set<User> getUser() {
        return user;
    }

    public void setUser(Set<User> user) {
        this.user = user;
    }
 
        
}
