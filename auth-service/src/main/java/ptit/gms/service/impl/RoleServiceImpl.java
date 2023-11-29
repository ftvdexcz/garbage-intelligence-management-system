package ptit.gms.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ptit.gms.constant.Constant;
import ptit.gms.dto.request.CreateRoleReqDto;
import ptit.gms.dto.response.RoleResDto;
import ptit.gms.exception.ApiException;
import ptit.gms.service.RoleService;
import ptit.gms.store.mysql.entity.RoleEntity;
import ptit.gms.store.mysql.repository.RoleRepository;
import ptit.gms.utils.GenerateUtils;

import java.util.List;

@Slf4j
@Service
public class RoleServiceImpl implements RoleService {
    @Autowired
    RoleRepository roleRepository;

    @Override
    public RoleResDto createRole(CreateRoleReqDto createRoleReqDto) {
        log.info("[RoleServiceImpl - createRole] createRoleReqDto: {}", createRoleReqDto);
        if(!Constant.X_USER_ROLE.equals(Constant.ADMIN_ROLE_TYPE)){
            throw ApiException.ErrUnauthorized().build();
        }

        if(roleRepository.existsByRoleType(createRoleReqDto.getRoleType()))
            throw ApiException.ErrExisted().message("role type đã tồn tại").build();

        String code = GenerateUtils.generateRandomRoleCode();
        RoleEntity roleEntity = RoleEntity.builder().
                roleName(createRoleReqDto.getRoleName()).
                roleType(createRoleReqDto.getRoleType()).
                build();

        roleRepository.save(roleEntity);

        return RoleResDto.builder().
                roleName(roleEntity.getRoleName()).
                roleType(roleEntity.getRoleType()).
                build();
    }

    @Override
    public List<RoleResDto> listRoles(){
        if(!Constant.X_USER_ROLE.equals(Constant.ADMIN_ROLE_TYPE)){
            throw ApiException.ErrUnauthorized().build();
        }
        return roleRepository.listRoles();
    }


    @Override
    public RoleResDto getRoleByType(int roleType){
        return roleRepository.findByType(roleType);
    }
}
