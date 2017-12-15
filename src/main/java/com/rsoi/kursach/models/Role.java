package com.rsoi.kursach.models;

import com.rsoi.kursach.repository.AuditingEntity;
import com.rsoi.kursach.repository.BaseEntity;
import java.io.Serializable;
import java.util.List;
import javax.persistence.*;

@Entity
@Table(name = "role")
@NamedQuery(name = "Role.findAll", query = "SELECT r FROM Role r")
public class Role extends AuditingEntity implements Serializable, BaseEntity{
    
    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "role_id")
    private Long roleId;
    
    @Column(name = "role_name")
    private String roleName;
    
    @Column(name = "role")
    private String code;
    
    @OneToMany(mappedBy = "role")
    private List<UserRole> userRoles;

    public Long getRoleId() {
        return roleId;
    }

    public String getRoleName() {
        return roleName;
    }

    public String getCode() {
        return code;
    }

    public List<UserRole> getUserRoles() {
        return userRoles;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setUserRoles(List<UserRole> userRoles) {
        this.userRoles = userRoles;
    }
    
    public UserRole addUserRoles(UserRole userRoles) {
		getUserRoles().add(userRoles);
		userRoles.setRole(this);

		return userRoles;
	}

	public UserRole removeUserRoles(UserRole userRoles) {
		getUserRoles().remove(userRoles);
		userRoles.setRole(null);

		return userRoles;
	}
        
}
