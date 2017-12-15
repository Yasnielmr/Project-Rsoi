package com.rsoi.kursach.repository;

import com.rsoi.kursach.dto.RoleFilterDto;
import com.rsoi.kursach.dto.RoleViewDto;
import java.util.List;

public interface RoleJdbcRepository {
    
    List<RoleViewDto> searchRoleList();

    Long searchRampAreaPageableTotalCount(RoleFilterDto filter);

    List<RoleViewDto> searchRampAreaPageable(RoleFilterDto filter);
    
}
