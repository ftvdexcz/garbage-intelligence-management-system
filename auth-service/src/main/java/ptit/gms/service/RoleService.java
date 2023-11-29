package ptit.gms.service;

import ptit.gms.dto.request.CreateRoleReqDto;
import ptit.gms.dto.response.RoleResDto;

import java.util.List;

public interface RoleService {
    RoleResDto createRole(CreateRoleReqDto createRoleReqDto);

    List<RoleResDto> listRoles();

    RoleResDto getRoleByType(int roleType);
}
