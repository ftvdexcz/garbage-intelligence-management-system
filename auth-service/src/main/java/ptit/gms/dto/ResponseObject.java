package ptit.gms.dto;

import lombok.*;
import ptit.gms.constant.CodeResponse;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ResponseObject {
    private int code;
    private String status;
    private String message;
    private Object data;

    public static ResponseObject resp(){
        return new ResponseObjectBuilder().
                code(CodeResponse.OK.code).
                status(CodeResponse.OK.status).
                message(CodeResponse.OK.message).
                build();
    }

    public ResponseObject setResponseCode(CodeResponse codeResponse){
        this.code = codeResponse.code;
        this.status = codeResponse.status;
        this.message = codeResponse.message;
        return this;
    }

    public ResponseObject setData(Object data){
        this.data = data;
        return this;
    }

    public ResponseObject setMessage(String message){
        this.message = message;
        return this;
    }
}


