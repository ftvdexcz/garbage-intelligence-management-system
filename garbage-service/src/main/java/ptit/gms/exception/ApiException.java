package ptit.gms.exception;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import ptit.gms.constant.CodeResponse;

@Getter
@Setter
@Builder
public class ApiException extends RuntimeException{
    private String message;
    private CodeResponse code;

    public static ApiExceptionBuilder ErrNotFound(){
        return new ApiExceptionBuilder().code(CodeResponse.NOT_FOUND);
    }

    public static ApiExceptionBuilder ErrFileIOException(){
        return new ApiExceptionBuilder().code(CodeResponse.FILE_IO_EXCEPTION);
    }

    public static ApiExceptionBuilder ErrDataLoss(){
        return new ApiExceptionBuilder().code(CodeResponse.DATA_LOSS);
    }

    public static ApiExceptionBuilder ErrUnauthorized(){
        return new ApiExceptionBuilder().code(CodeResponse.UNAUTHORIZED);
    }
}
