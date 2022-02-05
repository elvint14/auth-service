package az.et.authservice.exception;


import az.et.authservice.dto.response.BaseResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Optional;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BaseException.class)
    public ResponseEntity<BaseResponse<?>> handleBaseException(BaseException ex) {
        ex.printStackTrace();
        return ResponseEntity.status(
                Optional.ofNullable(
                        HttpStatus.resolve(
                                ex.getCode()
                        )
                ).orElse(HttpStatus.INTERNAL_SERVER_ERROR)
        ).body(BaseResponse.error(ex));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<BaseResponse<?>> handleException(Exception ex) {
        ex.printStackTrace();
        return ResponseEntity.status(
                HttpStatus.INTERNAL_SERVER_ERROR
        ).body(BaseResponse.error(ex));
    }

}
