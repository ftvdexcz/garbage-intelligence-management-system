package ptit.gms.Controller;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import ptit.gms.constant.CodeResponse;
import ptit.gms.dto.ResponseObject;
import ptit.gms.dto.request.AuthReqDto;
import ptit.gms.dto.response.AuthResDto;
import ptit.gms.dto.response.UserResDto;
import ptit.gms.exception.ApiException;
import ptit.gms.service.AuthService;
import ptit.gms.utils.ResponseUtils;

@RestController
public class AuthController {
    @Autowired
    AuthService authService;

    @PostMapping("/auth")
    public ResponseEntity<ResponseObject> auth(@RequestBody AuthReqDto authReqDto){
        AuthResDto authRes = authService.auth(authReqDto);

        return ResponseUtils.responseWithCode(CodeResponse.OK, authRes);
    }

    @GetMapping("/me")
    public ResponseEntity<ResponseObject> getCurrentUserInfo(WebRequest request, HttpServletResponse response){
        UserResDto user = authService.getCurrentUserInfo(request, response);

        return ResponseUtils.responseWithCode(CodeResponse.OK, user);
    }
}
