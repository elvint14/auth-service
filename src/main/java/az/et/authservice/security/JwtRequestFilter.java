package az.et.authservice.security;

import az.et.authservice.dto.request.JwtUserDetailsDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static az.et.authservice.constant.AuthHeader.X_USER;

@Component
@RequiredArgsConstructor
public class JwtRequestFilter extends OncePerRequestFilter {

    private final ObjectMapper objectMapper;

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest,
                                    HttpServletResponse httpServletResponse,
                                    FilterChain filterChain) throws ServletException, IOException {

        final String xUser = httpServletRequest.getHeader(X_USER);
        System.out.println("X-USER is -> " + xUser);
        if (xUser != null) {
            /*
             * Əgər belə bir User varsa, gəlib bura çıxacaq. Yəni X-User dəyərinin set
             * olunması Zuul da baş verir. Əgər bu dəyər null-dırsa, deməli belə User yoxdur və ya
             * authenticated məcbur olmayan url-lərə sorğu gəlir.
             */
            final JwtUserDetailsDto userDetailsDto = objectMapper.readValue(
                    xUser,
                    JwtUserDetailsDto.class
            );
            final JwtUserDetails jwtUserDetails = JwtUserDetails.create(
                    userDetailsDto.getId(),
                    userDetailsDto.getUsername(),
                    userDetailsDto.getPassword(),
                    userDetailsDto.getAuthorities()
            );
            final Authentication authentication = new UsernamePasswordAuthenticationToken(
                    jwtUserDetails,
                    "",
                    jwtUserDetails.getAuthorities()
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }
}