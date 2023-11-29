package ptit.gms.Controller;


import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ptit.gms.constant.CodeResponse;
import ptit.gms.dto.ResponseObject;
import ptit.gms.dto.request.CreateRoleReqDto;
import ptit.gms.dto.response.RoleResDto;
import ptit.gms.service.RoleService;
import ptit.gms.utils.ResponseUtils;
import java.util.List;


@Slf4j
@RestController
@RequestMapping("roles")
public class RoleController {
    @Autowired
    RoleService roleService;

    @PostMapping("")
    public ResponseEntity<ResponseObject> createRole(@Valid @RequestBody CreateRoleReqDto createRoleReqDto){
        RoleResDto roleResDto = roleService.createRole(createRoleReqDto);

        return ResponseUtils.responseWithCode(CodeResponse.CREATED, roleResDto);
    }

    @GetMapping("")
    public ResponseEntity<ResponseObject> listRoles(){
        List<RoleResDto> roles = roleService.listRoles();

        return ResponseUtils.responseWithCode(CodeResponse.OK, roles);
    }
}
