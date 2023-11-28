package ptit.gms.Controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ptit.gms.constant.CodeResponse;
import ptit.gms.dto.ResponseObject;
import ptit.gms.dto.request.CreateUserReqDto;
import ptit.gms.dto.response.CreateUserResDto;
import ptit.gms.dto.response.PaginationResDto;
import ptit.gms.dto.response.UserResDto;
import ptit.gms.service.UserService;
import ptit.gms.utils.ResponseUtils;

import java.util.List;

@Slf4j
@RestController("/users")
@RequestMapping("/users")
public class UserController {
    @Autowired
    UserService userService;

    @PostMapping("")
    public ResponseEntity<ResponseObject> createUser(@Valid @RequestBody CreateUserReqDto createUserReqDto){
        CreateUserResDto createUserResDto = userService.createUser(createUserReqDto);

        return ResponseUtils.responseWithCode(CodeResponse.CREATED, createUserResDto);
    }

    @GetMapping("")
    public ResponseEntity<ResponseObject> listUsers(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "") String username,
            @RequestParam(defaultValue = "") String email
    ){
        PaginationResDto<UserResDto> users = userService.listUsers(page - 1, size, username, email);

        return ResponseUtils.responseWithCode(CodeResponse.OK, users);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseObject> deleteBin(@PathVariable String id){
        userService.deleteUser(id);

        return ResponseUtils.responseWithCodeAndMsg(CodeResponse.OK, "Xóa thành công", null);
    }
}
