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

    public static ApiExceptionBuilder ErrInvalidArgument(){
        return new ApiExceptionBuilder().code(CodeResponse.INVALID_ARGUMENT);
    }

    public static ApiExceptionBuilder ErrExisted(){
        return new ApiExceptionBuilder().code(CodeResponse.EXISTED);
    }

    public static ApiExceptionBuilder ErrBadCredentials(){
        return new ApiExceptionBuilder().code(CodeResponse.BAD_CREDENTIALS);
    }

    public static ApiExceptionBuilder ErrForbidden(){
        return new ApiExceptionBuilder().code(CodeResponse.FORBIDDEN);
    }

    public static ApiExceptionBuilder ErrUnauthorized(){
        return new ApiExceptionBuilder().code(CodeResponse.UNAUTHORIZED);
    }
}
