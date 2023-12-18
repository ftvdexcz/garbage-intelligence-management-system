package ptit.gms.service;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.context.request.WebRequest;
import ptit.gms.dto.request.AuthReqDto;
import ptit.gms.dto.request.CctvAuthReqDto;
import ptit.gms.dto.response.AuthResDto;
import ptit.gms.dto.response.UserResDto;

public interface AuthService {
    AuthResDto auth(AuthReqDto authReqDto);

    UserResDto getCurrentUserInfo(WebRequest request, HttpServletResponse response);

    void cctvAuth(CctvAuthReqDto cctvAuthReqDto);
}
