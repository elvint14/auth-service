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
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static az.et.authservice.constant.AuthHeader.AUTHORIZATION;
import static az.et.authservice.constant.AuthHeader.BEARER;

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
        final String jwt = parseJwt(httpServletRequest);
        System.out.println(jwt);
        try {
            if (jwt != null && jwtTokenUtil.validateToken(jwt)) {
                userRepository.findByUsername(
                        jwtTokenUtil.getUsernameFromToken(jwt)
                ).ifPresent(userFromJwt -> {
                    System.out.println(userFromJwt);
                    if (checkAccessTokenIsExist(jwt, userFromJwt)) {
                        final Authentication auth = jwtTokenUtil.getAuthentication(jwt);
                        if (auth != null) SecurityContextHolder.getContext().setAuthentication(auth);
                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw BaseException.of(ErrorEnum.AUTH_ERROR);
        }
        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }

    private String parseJwt(HttpServletRequest request) {
        final String authHeader = request.getHeader(AUTHORIZATION);
        System.out.println(authHeader);
        if (StringUtils.hasText(authHeader) && authHeader.startsWith(BEARER)) {
            return authHeader.substring(7);
        }
        return null;
    }

    private boolean checkAccessTokenIsExist(String token, UserEntity user) {
//        return userTokensRepository.findAllByUserId(user.getId())
//                .stream()
//                .anyMatch(userLogin ->
//                        userLogin.getAccessToken().equals(DigestUtils.md5DigestAsHex(token.getBytes()))
//                );
        return userTokensRepository.existsUserTokensEntityByAccessTokenAndUser(
                DigestUtils.md5DigestAsHex(token.getBytes()),
                user
        );
    }
}