package az.et.authservice.exception;


import az.et.authservice.constant.ErrorEnum;

import static java.util.Objects.isNull;


public class BaseException extends RuntimeException{

    private static final long serialVersionUID = -943742523154447894L;
    private Integer code;
    private String message;

    public BaseException(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public BaseException(ErrorEnum errorEnum, Object replace){
        this.code = errorEnum.getCode();
        this.message = isNull(replace) ? errorEnum.getMessage() : String.format(errorEnum.getMessage(),replace);
    }

    public static BaseException of(ErrorEnum errorEnum, Object replace){
        return new BaseException(errorEnum, replace);
    }

    public static BaseException of(ErrorEnum errorEnum){
        return new BaseException(errorEnum, null);
    }

    public Integer getCode() {
        return code;
    }

    public BaseException setCode(Integer code) {
        this.code = code;
        return this;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public BaseException setMessage(String message) {
        this.message = message;
        return this;
    }
}
