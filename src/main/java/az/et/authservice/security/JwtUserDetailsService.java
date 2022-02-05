package az.et.authservice.security;

import az.et.authservice.entity.RoleEntity;
import az.et.authservice.constant.ErrorEnum;
import az.et.authservice.exception.BaseException;
import az.et.authservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class JwtUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        return userRepository.findByUsername(s)
                .map(user -> JwtUserDetails
                        .builder()
                        .username(s)
                        .password(user.getPassword())
                        .authorities(
                                user.getRoles() != null ?
                                        mapRoles(
                                                user.getRoles()
                                                        .stream()
                                                        .map(RoleEntity::getName)
                                                        .collect(Collectors.toList())
                                        ) : null
                        ).build()
                ).orElseThrow(() ->
                        BaseException.of(ErrorEnum.USERNAME_NOT_FOUND)
                );
    }

    private Collection<? extends GrantedAuthority> mapRoles(List<String> roles) {
        return roles.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }
}