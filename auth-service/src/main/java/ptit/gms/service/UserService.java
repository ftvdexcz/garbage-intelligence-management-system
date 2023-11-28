package ptit.gms.service;

import ptit.gms.dto.request.CreateUserReqDto;
import ptit.gms.dto.response.CreateUserResDto;
import ptit.gms.dto.response.PaginationResDto;
import ptit.gms.dto.response.UserResDto;

import java.util.List;

public interface UserService {
    CreateUserResDto createUser(CreateUserReqDto createUserReqDto);

    PaginationResDto<UserResDto> listUsers(int page, int size, String username, String email);

    void deleteUser(String id);
}
