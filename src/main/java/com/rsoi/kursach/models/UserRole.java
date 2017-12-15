package com.rsoi.kursach.models;

import com.rsoi.kursach.repository.AuditingEntity;
import com.rsoi.kursach.repository.BaseEntity;
import java.io.Serializable;
import java.util.Set;
import javax.persistence.*;

@Entity
@Table(name = "user_role")
public class UserRole extends AuditingEntity implements Serializable, BaseEntity{
    
    private static final long serialVersionUID = -3129928406058108033L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_role_id")    
    private Long userRoleId;
    
    @JoinColumn(name = "user_id", referencedColumnName = "user_id")
    @ManyToOne(optional = false)
    private User user;
    
    @JoinColumn(name = "role_id", referencedColumnName = "role_id")
    @ManyToOne(optional = false)
    private Role role;

    public Long getUserRoleId() {
        return userRoleId;
    }

    public User getUser() {
        return user;
    }

    public Role getRole() {
        return role;
    }

    public void setUserRoleId(Long userRoleId) {
        this.userRoleId = userRoleId;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setRole(Role role) {
        this.role = role;
    }
    
    
}
