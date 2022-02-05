package az.et.authservice.dto.response;

import az.et.authservice.exception.BaseException;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

import static java.util.Objects.isNull;


@JsonInclude(JsonInclude.Include.NON_NULL)
public class BaseResponse<T> implements Serializable {

    private static final long serialVersionUID = -5847326309897181692L;
    private String timestamp = String.valueOf(LocalDateTime.now().toEpochSecond(ZoneOffset.UTC));
    private MessageResponse messages;
    private Integer status;
    private T data;

    public BaseResponse() {
    }

    public BaseResponse(MessageResponse.ResponseType type, String description, Integer status, T data) {
        type = isNull(type) ? MessageResponse.ResponseType.ERROR : type;

        this.messages = new MessageResponse(type, description);
        this.status = status;
        this.data = data;
    }

    public static <T> BaseResponse<T> ok(T object) {
        return new BaseResponse<>(MessageResponse.ResponseType.SUCCESS, null, 200, object);
    }

    public static <T> BaseResponse<T> ok() {
        return new BaseResponse<>(MessageResponse.ResponseType.SUCCESS, null, 200, null);
    }

    public static <T> BaseResponse<T> error(BaseException ex){
        return new BaseResponse<>(null, ex.getMessage(), ex.getCode(), null);
    }

    public static <T> BaseResponse<T> error(Exception ex){
        return new BaseResponse<>(null, ex.getMessage(), 500, null);
    }

    public String getTimestamp() {
        return timestamp;
    }

    public BaseResponse<T> setTimestamp(String timestamp) {
        this.timestamp = timestamp;
        return this;
    }

    public MessageResponse getMessages() {
        return messages;
    }

    public BaseResponse<T> setMessages(MessageResponse messages) {
        this.messages = messages;
        return this;
    }

    public Integer getStatus() {
        return status;
    }

    public BaseResponse<T> setStatus(Integer status) {
        this.status = status;
        return this;
    }

    public T getData() {
        return data;
    }

    public BaseResponse<T> setData(T data) {
        this.data = data;
        return this;
    }
}
