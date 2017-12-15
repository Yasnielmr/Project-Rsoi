package com.rsoi.kursach.repository;

import com.rsoi.kursach.models.UserRole;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRoleJpaRepository extends CrudRepository<UserRole, Long>{
    
    UserRole save(UserRole userRole);
    
    @Query("select t " + " from UserRole t " + "where t.user.userId = :userId ")
    List<UserRole> findByUserId(@Param("userId") Long userId);
    
}
