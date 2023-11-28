package ptit.gms.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ptit.gms.dto.request.CreateRoleReqDto;
import ptit.gms.dto.response.RoleResDto;
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

        String code = GenerateUtils.generateRandomRoleCode();
        RoleEntity roleEntity = RoleEntity.builder().
                code(code).
                roleName(createRoleReqDto.getRoleName()).
                build();

        roleRepository.save(roleEntity);

        return RoleResDto.builder().
                code(roleEntity.getCode()).
                roleName(roleEntity.getRoleName()).
                build();
    }

    @Override
    public List<RoleResDto> listRoles(){
        return roleRepository.listRoles();
    }

    @Override
    public RoleResDto getRoleByCode(String roleCode){
        return roleRepository.findByCode(roleCode);
    }
}
