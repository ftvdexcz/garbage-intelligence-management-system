package ptit.gms.service.impl;

import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.WebRequest;
import ptit.gms.config.Config;
import ptit.gms.constant.Constant;
import ptit.gms.dto.request.AuthReqDto;
import ptit.gms.dto.response.AuthResDto;
import ptit.gms.dto.response.UserResDto;
import ptit.gms.exception.ApiException;
import ptit.gms.service.AuthService;
import ptit.gms.store.mysql.entity.UserEntity;
import ptit.gms.store.mysql.repository.UserRepository;
import ptit.gms.store.redis.repository.RedisRepository;
import ptit.gms.utils.JsonUtils;
import ptit.gms.utils.TimeUtils;
import ptit.gms.utils.auth.JwtTokenProvider;
import ptit.gms.utils.auth.PasswordUtils;

@Slf4j
@Service
public class AuthServiceImpl implements AuthService {
    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordUtils passwordUtils;

    @Autowired
    JwtTokenProvider jwtTokenProvider;

    @Autowired
    RedisRepository redisRepository;

    @Autowired
    Config config;

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
        String apiKey = request.getHeader(Constant.X_API_KEY_HEADER);
        if(apiKey != null){
            if(apiKey.equals(config.getApiKey())){
                response.addHeader(Constant.X_USER_ROLE_HEADER, Constant.INTERNAL_ROLE_TYPE);
                return null;
            }
        }

        String accessToken = jwtTokenProvider.parseAccessToken(request);
        log.info("[AuthServiceImpl - getCurrentUserInfo] accessToken: {}", accessToken);
        Object accessTokenCache =  redisRepository.getKeyValue(accessToken);

        String userId = "";
        if(accessTokenCache != null){
            userId = accessTokenCache.toString();
            log.info("[AuthServiceImpl - getCurrentUserInfo] hit cache - accessTokenCache: {}", userId);
        }else{
            userId = jwtTokenProvider.validateToken(accessToken);
        }

        UserResDto user = null;
        Object userCache = redisRepository.getKeyValue(userId);
        if(userCache != null){
            user = (UserResDto) userCache;
            // get cached timestamp in ms = cur - (TTL - Remain TTL)
            long keyCachedTimestamp = TimeUtils.getCurrentTimestampMs() - (Constant.REDIS_TTL_USER - redisRepository.getKeyExpire(userId) * 1000);
            log.info("[AuthServiceImpl - getCurrentUserInfo] hit user cache - keyCachedTimestamp: {}, userCached: {}", keyCachedTimestamp, user);
        }else{
            user = userRepository.getByUserId(userId);
            redisRepository.setKeyValueExpire(userId, user, Constant.REDIS_TTL_USER);
        }

        // in case token is valid but user is deleted
        if(user == null){
            throw ApiException.ErrForbidden().build();
        }

        // forward header X-USER-ID, X-USER-ROLE to internal service
        response.addHeader(Constant.X_USER_ID_HEADER, user.getId());
        response.addHeader(Constant.X_USER_ROLE_HEADER, String.valueOf(user.getRole().getRoleType()));

        return user;
    }
}
