package az.et.authservice.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.Optional;

import static java.util.Objects.isNull;


public class MessageResponse implements Serializable {

    public enum ResponseType {
        SUCCESS("Successfully finished operation"),
        ERROR("Error"),
        API_ERROR("Called API Service returned error");

        private final String message;

        ResponseType(String message) {
            this.message = message;
        }

        public String getMessage() {
            return message;
        }
    }

    private static final long serialVersionUID = -5844117795568820413L;
    @JsonProperty("type")
    private String messageType;
    @JsonProperty("message")
    private String message;

    public MessageResponse(ResponseType type, String description) {
        this.messageType = type.name();
        this.message = isNull(description)
                ? Optional.ofNullable(type.getMessage()).orElse("null reference")
                : description;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessageType() {
        return messageType;
    }

    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }

}