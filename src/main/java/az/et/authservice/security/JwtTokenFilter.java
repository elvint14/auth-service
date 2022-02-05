package az.et.authservice.security;

import az.et.authservice.constant.ErrorEnum;
import az.et.authservice.entity.UserEntity;
import az.et.authservice.exception.BaseException;
import az.et.authservice.repository.UserRepository;
import az.et.authservice.repository.UserTokensRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtTokenFilter extends OncePerRequestFilter {

    private final JwtUserDetailsService userDetailsService;
    private final UserRepository userRepository;
    private final UserTokensRepository userTokensRepository;

    private final JwtTokenUtil jwtTokenUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest,
                                    HttpServletResponse httpServletResponse,
                                    FilterChain filterChain) throws ServletException, IOException {
        final String jwt = httpServletRequest.getHeader("Authorization");
        try {
            if (jwt != null && jwtTokenUtil.validateToken(jwt)) {
                userRepository.findByUsername(
                        jwtTokenUtil.getUsernameFromToken(jwt)
                ).ifPresent(userFromJwt -> {
                    if (checkAccessTokenIsExist(jwt, userFromJwt)) {
                        final Authentication auth = jwtTokenUtil.getAuthentication(jwt);
                        if (auth != null) SecurityContextHolder.getContext().setAuthentication(auth);
                    }
                });
            }
        } catch (Exception e) {
            throw BaseException.of(ErrorEnum.AUTH_ERROR);
        }
        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }

    private boolean checkAccessTokenIsExist(String token, UserEntity user) {
//        return userTokensRepository.findAllByUserId(user.getId())
//                .stream()
//                .anyMatch(userLogin ->
//                        userLogin.getAccessToken().equals(DigestUtils.md5DigestAsHex(token.getBytes()))
//                );
        return userTokensRepository.existsUserTokensEntityByUserAndAccessToken(
                user,
                token
        );
    }
}