package com.rsoi.kursach.repository;

import com.rsoi.kursach.models.Role;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleJpaRepository extends CrudRepository<Role, Long>{
    
    Role save(Role Role);

    @Query("SELECT r FROM Role r WHERE r.code = :code")
    List<Role> findByCode(@Param("code") String code);
    
}
