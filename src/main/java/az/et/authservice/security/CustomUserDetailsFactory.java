package az.et.authservice.security;

import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;
import java.util.stream.Collectors;

public class CustomUserDetailsFactory {

    public static JwtUserDetails create(String username, String password, List<String> roles) {
        return JwtUserDetails.builder()
                .username(username)
                .password(password)
                .authorities(
                        roles.stream()
                                .map(SimpleGrantedAuthority::new)
                                .collect(Collectors.toList())
                ).build();
    }

    public static JwtUserDetails create(String username, List<String> roles) {
        return JwtUserDetails.builder()
                .username(username)
                .authorities(
                        roles.stream()
                                .map(SimpleGrantedAuthority::new)
                                .collect(Collectors.toList())
                ).build();
    }
}
