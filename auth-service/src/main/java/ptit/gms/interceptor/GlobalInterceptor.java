package ptit.gms.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import ptit.gms.constant.Constant;


@Slf4j
@Component
public class GlobalInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Constant.X_USER_ID = request.getHeader(Constant.X_USER_ID_HEADER);
        Constant.X_USER_ROLE = request.getHeader(Constant.X_USER_ROLE_HEADER);

        if(Constant.X_USER_ID == null)
            Constant.X_USER_ID = "";

        if(Constant.X_USER_ROLE == null)
            Constant.X_USER_ROLE = "";

        log.info("[GlobalInterceptor - preHandler] X-USER-ID: {}, X-USER-ROLE: {}", Constant.X_USER_ID, Constant.X_USER_ROLE);
        return HandlerInterceptor.super.preHandle(request, response, handler);
    }
}



