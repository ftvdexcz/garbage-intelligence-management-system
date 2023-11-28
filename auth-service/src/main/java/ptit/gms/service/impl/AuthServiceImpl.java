package ptit.gms.service.impl;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.WebRequest;
import ptit.gms.constant.Constant;
import ptit.gms.dto.request.AuthReqDto;
import ptit.gms.dto.response.AuthResDto;
import ptit.gms.dto.response.UserResDto;
import ptit.gms.exception.ApiException;
import ptit.gms.service.AuthService;
import ptit.gms.store.mysql.entity.UserEntity;
import ptit.gms.store.mysql.repository.UserRepository;
import ptit.gms.utils.auth.JwtTokenProvider;
import ptit.gms.utils.auth.PasswordUtils;

@Service
public class AuthServiceImpl implements AuthService {
    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordUtils passwordUtils;

    @Autowired
    JwtTokenProvider jwtTokenProvider;

    @Override
    public AuthResDto auth(AuthReqDto authReqDto) {
        UserEntity userEntity = userRepository.findByEmailAndStatus(authReqDto.getEmail(), Constant.STATUS_USER_ACTIVE);

        if(userEntity == null){
            throw ApiException.ErrBadCredentials().build();
        }

        if(!passwordUtils.decryptPassword(authReqDto.getPassword(), userEntity.getPassword())){
            throw ApiException.ErrBadCredentials().build();
        }

        String accessToken = jwtTokenProvider.signToken(userEntity.getId());
        return AuthResDto.builder().
                token(accessToken).
                userId(userEntity.getId()).
                build();
    }

    @Override
    public UserResDto getCurrentUserInfo(WebRequest request, HttpServletResponse response) {
        String accessToken = jwtTokenProvider.parseAccessToken(request);

        String userId = jwtTokenProvider.validateToken(accessToken);

        UserResDto user = userRepository.getByUserId(userId);
        // in case token is valid but user is deleted
        if(user == null){
            throw ApiException.ErrForbidden().build();
        }

        // forward header X-USER-ID to internal service
        response.addHeader(Constant.X_USER_ID_HEADER, user.getId());

        return user;
    }
}
