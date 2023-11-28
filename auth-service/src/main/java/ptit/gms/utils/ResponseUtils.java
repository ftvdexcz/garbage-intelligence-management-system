package ptit.gms.utils;

import org.springframework.http.ResponseEntity;
import ptit.gms.constant.CodeResponse;
import ptit.gms.dto.ResponseObject;

public class ResponseUtils {
    public static ResponseEntity<ResponseObject> responseWithCode(CodeResponse codeResponse, Object data){
        return ResponseEntity.status(codeResponse.code).body(
                ResponseObject.resp().setResponseCode(codeResponse).setData(data)
        );
    }

    public static ResponseEntity<ResponseObject> responseWithMsg(String msg, Object data){
        return ResponseEntity.ok().body(
                ResponseObject.resp().setMessage(msg).setData(data)
        );
    }

    public static ResponseEntity<ResponseObject> responseWithCodeAndMsg(CodeResponse codeResponse, String msg, Object data){
        return ResponseEntity.status(codeResponse.code).body(
                ResponseObject.resp().setResponseCode(codeResponse).setMessage(msg).setData(data)
        );
    }
}
