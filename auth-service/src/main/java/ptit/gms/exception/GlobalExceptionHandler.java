package ptit.gms.exception;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import ptit.gms.config.Config;
import ptit.gms.constant.CodeResponse;


@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
    @Autowired
    Config config;

    @ExceptionHandler(ApiException.class)
    public ResponseEntity<ErrorResponse> handleApiException(ApiException err, WebRequest request) {
        ErrorResponse errorResponse = new ErrorResponse(
                err.getCode().status,
                err.getCode().code,
                err.getCode().message
        );

        // override default message with custom message
        if(err.getMessage() != null && !err.getMessage().strip().equals("")){
            errorResponse.setMessage(err.getMessage());
        }

        if(config.getEnv().equals("dev") && ExceptionUtils.isTraceOn(request)){
            errorResponse.setStackTrace(ExceptionUtils.getStackTrace(err));
        }
        return ResponseEntity.status(err.getCode().code).body(errorResponse);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        CodeResponse code = CodeResponse.INVALID_ARGUMENT;
        ErrorResponse errorResponse = new ErrorResponse(
                code.status,
                code.code,
                code.message
        );

        if(config.getEnv().equals("dev")){
            errorResponse.setDevMessage(ex.getMessage());
            if(ExceptionUtils.isTraceOn(request))
                errorResponse.setStackTrace(ExceptionUtils.getStackTrace(ex));
        }

        for (FieldError fieldError: ex.getBindingResult().getFieldErrors()) {
            errorResponse.addValidationError(fieldError.getField(),
                    fieldError.getDefaultMessage());
        }
        return ResponseEntity.status(code.code).body(errorResponse);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleAllUncaughtException(Exception err, WebRequest request){
        CodeResponse code = CodeResponse.INTERNAL;

        ErrorResponse errorResponse = new ErrorResponse(
                code.status,
                code.code,
                code.message
        );

        if(config.getEnv().equals("dev")){
            errorResponse.setDevMessage(err.getMessage());
            if(ExceptionUtils.isTraceOn(request))
                errorResponse.setStackTrace(ExceptionUtils.getStackTrace(err));
        }

        return ResponseEntity.status(code.code).body(errorResponse);
    }

}
