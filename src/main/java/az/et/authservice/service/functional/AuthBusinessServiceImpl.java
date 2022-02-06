package az.et.authservice.service.functional;

import az.et.authservice.constant.ErrorEnum;
import az.et.authservice.dto.request.LoginRequestDto;
import az.et.authservice.dto.response.ResponseTokenDto;
import az.et.authservice.entity.UserEntity;
import az.et.authservice.entity.UserTokensEntity;
import az.et.authservice.exception.BaseException;
import az.et.authservice.security.JwtTokenUtil;
import az.et.authservice.service.AuthBusinessService;
import az.et.authservice.service.UserService;
import az.et.authservice.service.UserTokensService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import static az.et.authservice.constant.ErrorEnum.USERNAME_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class AuthBusinessServiceImpl implements AuthBusinessService {
    private final UserService userService;
    private final JwtTokenUtil jwtTokenUtil;
    private final UserTokensService userTokensService;
    private final AuthenticationManager authenticationManager;

    @Override
    public ResponseTokenDto login(LoginRequestDto loginDto) {
        final String username = loginDto.getUsername();
        authenticate(loginDto);
        final UserEntity user = userService.findByUsernameOrThrowEx(username, USERNAME_NOT_FOUND);
        final String accessToken = jwtTokenUtil.createAccessToken(user);
        final String refreshToken = jwtTokenUtil.createRefreshToken(user, loginDto.isRememberMe());
        saveUserLogin(accessToken, refreshToken, user);
        return ResponseTokenDto
                .builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    private void saveUserLogin(String accessToken, String refreshToken, UserEntity user) {
        final UserTokensEntity userTokensEntity = userTokensService.findByUserIdOrNew(user.getId());
        userTokensEntity.setAccessToken(DigestUtils.md5DigestAsHex(accessToken.getBytes()));
        userTokensEntity.setRefreshToken(DigestUtils.md5DigestAsHex(refreshToken.getBytes()));
        userTokensEntity.setUser(user);
        userTokensService.insertOrUpdate(userTokensEntity);
    }

    private void authenticate(LoginRequestDto loginRequestDto) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequestDto.getUsername(),
                            loginRequestDto.getPassword()
                    )
            );
        } catch (AuthenticationException e) {
            throw BaseException.of(ErrorEnum.AUTH_ERROR);
        }
    }

}
