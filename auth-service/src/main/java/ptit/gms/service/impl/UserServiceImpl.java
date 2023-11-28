package ptit.gms.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ptit.gms.constant.Constant;
import ptit.gms.dto.request.CreateUserReqDto;
import ptit.gms.dto.response.CreateUserResDto;
import ptit.gms.dto.response.PaginationResDto;
import ptit.gms.dto.response.RoleResDto;
import ptit.gms.dto.response.UserResDto;
import ptit.gms.exception.ApiException;
import ptit.gms.service.RoleService;
import ptit.gms.service.UserService;
import ptit.gms.store.mysql.entity.UserEntity;
import ptit.gms.store.mysql.repository.UserRepository;
import ptit.gms.utils.GenerateUtils;
import ptit.gms.utils.auth.PasswordUtils;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleService roleService;

    @Autowired
    PasswordUtils passwordUtils;

    @Override
    public CreateUserResDto createUser(CreateUserReqDto createUserReqDto) {
        log.info("[UserServiceImpl - createUser] createUserReqDto: {}", createUserReqDto);

        boolean existedEmail = userRepository.existsByEmailAndStatus(createUserReqDto.getEmail(), Constant.STATUS_USER_ACTIVE);
        if (existedEmail){
            throw ApiException.ErrExisted().message("Email đã tồn tại").build();
        }

        RoleResDto role = roleService.getRoleByCode(createUserReqDto.getRole());
        if (role == null){
            throw ApiException.ErrInvalidArgument().message("role (mã role) không hợp lệ").build();
        }

        String id = GenerateUtils.generateRandomUUID();
        UserEntity userEntity = UserEntity.builder().
                id(id).
                username(createUserReqDto.getUsername()).
                password(passwordUtils.encryptPassword(createUserReqDto.getPassword())).
                phone(createUserReqDto.getPhone()).
                email(createUserReqDto.getEmail()).
                status(Constant.STATUS_USER_ACTIVE).
                roleCode(createUserReqDto.getRole()).
                build();

        userRepository.save(userEntity);

        return CreateUserResDto.builder().
                id(userEntity.getId()).
                username(userEntity.getUsername()).
                phone(userEntity.getPhone()).
                email(userEntity.getEmail()).
                role(role).
                build();
    }

    @Override
    public PaginationResDto<UserResDto> listUsers(int page, int size, String username, String email){
        log.info("[UserServiceImpl - listUsers] page: {}, size: {}, username: {}, email: {}", page, size, username, email);
        Pageable paginationOption = PageRequest.of(page, size, Sort.by(List.of(
                new Sort.Order(Sort.Direction.DESC, "createdDate")
        )));
        Page<UserResDto> userResDtoPage = userRepository.paginationUsers(username, email, paginationOption);

        return PaginationResDto.<UserResDto>builder().totals(userResDtoPage.getTotalPages()).
                pages(userResDtoPage.getTotalPages()).
                page(page + 1).
                size(size).
                results(userResDtoPage.getContent()).
                build();
    }

    @Override
    public void deleteUser(String id) {
        log.info("[UserServiceImpl - deleteUser] id: {}", id);
        Optional<UserEntity> user = userRepository.findById(id);
        if(user.isEmpty()){
            throw ApiException.ErrNotFound().message(String.format("user id %s không tồn tại", id)).build();
        }

        UserEntity userEntity = user.get();
        userEntity.setStatus(Constant.STATUS_USER_INACTIVE);
        userRepository.save(userEntity);
    }
}
