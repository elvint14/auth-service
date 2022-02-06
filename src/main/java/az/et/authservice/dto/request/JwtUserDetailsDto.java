package az.et.authservice.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
public class JwtUserDetailsDto implements Serializable {
    private static final long serialVersionUID = 5926468583005150707L;

    private Long id;
    private String username;
    private String password;
    private List<String> authorities;

    public static JwtUserDetailsDto of(Long id, String username, String password, List<String> roles) {
        return new JwtUserDetailsDto(
                id,
                username,
                password,
                roles
        );
    }
}
