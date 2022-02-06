package az.et.authservice.security;

import az.et.authservice.constant.ErrorEnum;
import az.et.authservice.exception.BaseException;
import az.et.authservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JwtUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username)
                .map(user -> JwtUserDetails.of(
                                user.getId(),
                                user.getUsername(),
                                user.getPassword(),
                                user.getRoles()
                        )
                ).orElseThrow(
                        () -> BaseException.of(ErrorEnum.USERNAME_NOT_FOUND)
                );
    }
}