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
import ptit.gms.store.redis.repository.RedisRepository;
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

    @Autowired
    RedisRepository redisRepository;

    @Override
    public CreateUserResDto createUser(CreateUserReqDto createUserReqDto) {
        log.info("[UserServiceImpl - createUser] createUserReqDto: {}", createUserReqDto);
        if(!Constant.X_USER_ROLE.equals(Constant.ADMIN_ROLE_TYPE) && !Constant.X_USER_ROLE.equals(Constant.USER_ROLE_TYPE)){
            throw ApiException.ErrUnauthorized().build();
        }

        boolean existedEmail = userRepository.existsByEmailAndStatus(createUserReqDto.getEmail(), Constant.STATUS_USER_ACTIVE);
        if (existedEmail){
            throw ApiException.ErrExisted().message("Email đã tồn tại").build();
        }

        if(Constant.X_USER_ROLE.equals(Constant.USER_ROLE_TYPE))
            createUserReqDto.setRoleType(Integer.valueOf(Constant.USER_ROLE_TYPE));

        RoleResDto role = roleService.getRoleByType(createUserReqDto.getRoleType());
        if (role == null){
            throw ApiException.ErrInvalidArgument().message("role_type không hợp lệ").build();
        }

        String id = GenerateUtils.generateRandomUUID();
        UserEntity userEntity = UserEntity.builder().
                id(id).
                username(createUserReqDto.getUsername()).
                password(passwordUtils.encryptPassword(createUserReqDto.getPassword())).
                phone(createUserReqDto.getPhone()).
                email(createUserReqDto.getEmail()).
                status(Constant.STATUS_USER_ACTIVE).
                roleType(createUserReqDto.getRoleType()).
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
        if(!Constant.X_USER_ROLE.equals(Constant.ADMIN_ROLE_TYPE)){
            throw ApiException.ErrUnauthorized().build();
        }

        Pageable paginationOption = PageRequest.of(page, size, Sort.by(List.of(
                new Sort.Order(Sort.Direction.DESC, "createdDate")
        )));
        Page<UserResDto> userResDtoPage = userRepository.paginationUsers(username, email, paginationOption);

        return PaginationResDto.<UserResDto>builder().totals(userResDtoPage.getTotalElements()).
                pages(userResDtoPage.getTotalPages()).
                page(page + 1).
                size(size).
                results(userResDtoPage.getContent()).
                build();
    }

    @Override
    public void deleteUser(String id) {
        log.info("[UserServiceImpl - deleteUser] id: {}", id);
        if(!Constant.X_USER_ROLE.equals(Constant.ADMIN_ROLE_TYPE)){
            if(!Constant.X_USER_ID.equals(id)){
                throw ApiException.ErrUnauthorized().build();
            }
        }

        UserEntity user = userRepository.findByIdAndStatus(id, Constant.STATUS_USER_ACTIVE);
        if(user == null){
            throw ApiException.ErrNotFound().message(String.format("user id %s không tồn tại", id)).build();
        }

        user.setStatus(Constant.STATUS_USER_INACTIVE);
        userRepository.save(user);

        // sync redis
        redisRepository.deleteKey(user.getId());
    }
}
