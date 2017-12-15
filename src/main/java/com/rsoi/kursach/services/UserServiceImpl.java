package com.rsoi.kursach.services;

import com.rsoi.kursach.dto.PageableResult;
import com.rsoi.kursach.dto.RolDto;
import com.rsoi.kursach.dto.RoleViewDto;
import com.rsoi.kursach.dto.UserDto;
import com.rsoi.kursach.dto.UserFilterDto;
import com.rsoi.kursach.dto.UserViewDto;
import com.rsoi.kursach.enums.StatusEnum;
import com.rsoi.kursach.enums.UserStatusEnum;
import com.rsoi.kursach.models.User;
import com.rsoi.kursach.models.UserRole;
import com.rsoi.kursach.repository.RoleJpaRepository;
import com.rsoi.kursach.repository.UserJdbcRepository;
import com.rsoi.kursach.repository.UserJpaRepository;
import com.rsoi.kursach.repository.UserRolJpaRepository;
import com.rsoi.kursach.repository.UserRoleJpaRepository;
import com.rsoi.kursach.repository.UsersCustomRepository;
import com.rsoi.kursach.support.WhereParams;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserServiceImpl implements UserService{
    
    @Autowired
    DataSource dataSource;
    
    @Autowired
    UserJpaRepository userJpaRepository;
    
    @Autowired
    UsersCustomRepository usersCustomRepository;

    @Autowired
    UserRolJpaRepository userRolJpaRepository;

    @Autowired
    UserJdbcRepository userJdbcRepository;

    @Autowired
    UserRoleJpaRepository userRoleJpaRepository;

    @Autowired
    RoleJpaRepository roleJpaRepository;


    private NamedParameterJdbcTemplate jdbcTemplate;

    @PostConstruct
    public void init(){
        jdbcTemplate= new NamedParameterJdbcTemplate(dataSource);
    }
	
    @Override
    public PageableResult find(UserFilterDto filter) {
        PageableResult result= new PageableResult();
        result.setResultList(new ArrayList<>());
        long totalCount=userJdbcRepository.searchPageableTotalCount(filter);
        result.setTotal(totalCount);

        if(totalCount>0){
            List<UserViewDto> items= userJdbcRepository.searchPageable(filter);

            for(UserViewDto item:items){
                item.setStatus(StatusEnum.findByCode(item.getStatus()).getLabel());
            }

            result.setResultList(items);
        }

        return result;
    }

    @Override
    public UserDto findById(long userId) {
        UserDto dto= new UserDto();
        User entity= userJpaRepository.findOne(userId);

        dto.setId(entity.getUserId());
        dto.setUserName(entity.getUserName());

        dto.setFirstName(entity.getFirstName());
        dto.setLastName(entity.getLastName());
        dto.setEmail(entity.getEmail());
        dto.setStatus(entity.getStatus().getCode());
        dto.setPassword(entity.getPassword());
        dto.setConfirmPassword(entity.getPassword());
        dto.setTitle(entity.getTitle());

        List<UserRole> roles= entity.getUserRoles();


        if(roles!=null){
         RoleViewDto roleDto;
         List<RoleViewDto> lstRoles= new ArrayList<>();
            for(UserRole item: roles){
                roleDto= new RoleViewDto();
                roleDto.setRoleId(item.getRole().getRoleId());
                roleDto.setRoleName(item.getRole().getRoleName());
                lstRoles.add(roleDto);
            }

          dto.setRoleList(lstRoles);
        }


        return dto;
    }

    @Override
    public UserDto findByUsername(String userName) {
       User entity= userJpaRepository.findByUserName(userName);
       return findById(entity.getUserId());
    }

    @Override
    public Long mergeUser(UserDto dto) {

        if(userJpaRepository.findDistinct(dto.getUserName(),dto.getId()==null?-1:dto.getId() )!=null){
            return (long) -1;
        }

        User entity;
        if(dto.getId()==null)
            entity= new User();
        else entity= userJpaRepository.findOne(dto.getId());

        entity.setUserName(dto.getUserName());
        //entity.setUser(userJpaRepository.findOne(dto.getManagerId()));
        entity.setFirstName(dto.getFirstName());
        entity.setEmail(dto.getEmail());
        entity.setLastName(dto.getLastName());
        entity.setStatus(UserStatusEnum.findByCode(dto.getStatus()));
        entity.setPassword(dto.getPassword());
        entity.setTitle(dto.getTitle());

        userJpaRepository.save(entity);

        //LOG.info("Saving Roles...");
        if(entity.getUserRoles()!=null)
            for(UserRole existent:entity.getUserRoles())
            userRoleJpaRepository.delete(existent);

        UserRole userRole;
        for(RoleViewDto item: dto.getRoleList()){
            userRole= new UserRole();
            userRole.setUser(entity);
            userRole.setRole(roleJpaRepository.findOne(item.getRoleId()));
            userRoleJpaRepository.save(userRole);
        }


        // LOG.info("Processed USER Id <"+entity.getUserId()+">");
        return entity.getUserId();
    }

    @Override
    public Long updateProfile(UserDto dto) {
        User entity=userJpaRepository.findOne(dto.getId());

        if(!userJdbcRepository.validCurrentPassword(dto.getUserName(),dto.getPassword())){
            return (long) -1;
        }

        entity.setTitle(dto.getTitle());
        entity.setEmail(dto.getEmail());
        if(dto.getNewPassword()!=null)
         entity.setPassword(dto.getNewPassword());

        userJpaRepository.save(entity);
        
        return entity.getUserId();
    }

    @Override
    public String deleteUser(long userId) {
       //LOG.info("verifying constraints with: Manager, Customer, Rfq (pricingAnalyst), Rfq (salesAccountManager)");

        if(!userJpaRepository.findManagerReference(userId).isEmpty())
            return "User is referenced as Manager";

        userJpaRepository.delete(userId);
        //LOG.info("User ".concat(String.valueOf(userId)).concat(" Deleted successfully"));
        return "success";
    }

    @Override
    public List<UserViewDto> searchUser(UserFilterDto value) {
        StringBuffer sql= new StringBuffer();
        sql.append("select \n");
        sql.append("  tu.user_id id, \n");
        sql.append("  tu.user_name name, \n");
        sql.append("  tu.first_name firstName, \n");
        sql.append("  tu.last_name lastName, \n");
        sql.append("  (tu.first_name || ' ' ||tu.last_name) fullName \n");
        sql.append("from users tu \n");
        sql.append("inner join user_role tur \n");
        sql.append("on tur.user_id = tu.user_id \n");
        sql.append("inner join role r \n");
        sql.append("on tur.role_id = r.role_id \n");

        sql.append("where 1=1 \n");
        WhereParams params= new WhereParams();

        sql.append(params.filter(" and upper(tu.first_name || ' '|| tu.last_name) like upper('%'|| :name ||'%') ",value.getName()));
        sql.append(params.filter(" and tu.status = :status ", StatusEnum.ACTIVE.getCode()));
        sql.append(params.filter(" and r.code = :roleCode ", value.getRole().getCode()));
        return jdbcTemplate.query(sql.toString(),params.getParams(),new BeanPropertyRowMapper<>(UserViewDto.class));
    }

    @Override
    public List<UserViewDto> searchManager(String query) {
        StringBuffer sql= new StringBuffer();

        sql.append("select \n");
        sql.append("  tu.user_id id, \n");
        sql.append("  tu.user_name name, \n");
        sql.append("  tu.first_name firstName, \n");
        sql.append("  tu.last_name lastName \n");
        sql.append("from users tu \n");
        sql.append("where 1=1 \n");
        WhereParams params= new WhereParams();
        sql.append(params.filter(" and upper(tu.first_name || ' '|| tu.last_name) like upper('%'|| :name ||'%') ",query));

        return jdbcTemplate.query(sql.toString(),params.getParams(),new BeanPropertyRowMapper<>(UserViewDto.class));
    }

    @Override
    @Transactional
    public int inactive(String username) {
        int numRecs = userJpaRepository.setAsInactive(username);
        assert numRecs == 1;
        return numRecs;
    }

    @Override
    @Transactional
    public int locked(String username) {
        int numRecs = userJpaRepository.setAsLocked(username);
        assert numRecs == 1;
        return numRecs;
    }

    public UserDto loadUserByUsername(String username){
        UserDto result = usersCustomRepository.findByUserName(username);
        if (result != null) {
            List<UserRole> rolList =
                    userRolJpaRepository.findByUserId(result.getId());

            for (UserRole userRole : rolList) {

                RolDto rolDto=new RolDto();

                rolDto.setId(userRole.getRole().getRoleId());
                rolDto.setCode(userRole.getRole().getCode());
                rolDto.setRoleName(userRole.getRole().getRoleName());

                result.addRol(rolDto);

            }
        }

        return result;
    }
    
}
