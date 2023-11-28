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
        log.info("[GlobalInterceptor - preHandler] X-USER-ID: {}", Constant.X_USER_ID);
        return HandlerInterceptor.super.preHandle(request, response, handler);
    }
}



