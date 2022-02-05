package az.et.authservice.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RequestCreateUserDto {
    @NotNull
    private String fullName;
    @NotNull
    @Email
    private String username;
    @NotNull
    private String password;
}