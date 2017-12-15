package com.rsoi.kursach.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.rsoi.kursach.enums.UserStatusEnum;
import com.rsoi.kursach.repository.AuditingEntity;
import com.rsoi.kursach.repository.BaseEntity;
import java.io.Serializable;
import java.util.List;
import javax.persistence.*;
import org.hibernate.annotations.Type;

@Entity
@Table(name = "users")
@NamedQuery(name = "User.findAll", query = "SELECT u FROM User u")
public class User extends AuditingEntity implements BaseEntity, Serializable{
    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userId;
    
    @Column(name = "firstName")
    private String firstName;
    
    @Column(name = "userName")
    private String userName;
    
    @Column(name = "last_Name")
    private String lastName;
    
    @Column(name = "email")
    private String email;
    private String password;
    private String title;
    
    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
    @JsonIgnore
    private List<UserRole> userRoles;
    
    @Column(name = "status")
    @Type(type = "pe.joedayz.api.enums.GenericEnumUserType", parameters = {
          @org.hibernate.annotations.Parameter(name = "enumClass", value = "pe.joedayz.api.enums.UserStatusEnum") })
    private UserStatusEnum status;



    public User() {
    }

    public User(Long userId) {
        this.userId = userId;
    }
    
    public String getFullName() {
		return firstName + " " + lastName;
    }
    

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getFirstName() {
        return firstName;
    }
    
    public long getUserId() {
	return this.userId;
   }
    
    public void setUserId(long userId){
		this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }

    public UserStatusEnum getStatus() {
        return status;
    }

    public void setStatus(UserStatusEnum status) {
        this.status = status;
    }
    
    public String getTitle() {
        return title;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setTitle(String title) {
        this.title = title;
    }
    
    public String getFirstname() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setFirstname(String firstname) {
        this.firstName = firstname;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getUsername() {
        return userName;
    }

    public String getEmail() {
        return email;
    }

    public void setUsername(String username) {
        this.userName = username;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<UserRole> getUserRoles() {
        return userRoles;
    }

    public void setUserRoles(List<UserRole> userRoles) {
        this.userRoles = userRoles;
    }
    
    public UserRole addUserRoles(UserRole userRoles) {
		getUserRoles().add(userRoles);
		userRoles.setUser(this);

		return userRoles;
	}

	public UserRole removeUserRoles(UserRole userRoles) {
		getUserRoles().remove(userRoles);
		userRoles.setUser(null);

		return userRoles;
	}
   
}
