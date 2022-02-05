package az.et.authservice.service.functional;

import az.et.authservice.constant.ErrorEnum;
import az.et.authservice.constant.UserStatusEnum;
import az.et.authservice.dto.request.RequestCreateUserDto;
import az.et.authservice.entity.RoleEntity;
import az.et.authservice.entity.UserEntity;
import az.et.authservice.exception.BaseException;
import az.et.authservice.security.CustomJwtUserDetailsFactory;
import az.et.authservice.security.JwtUserDetails;
import az.et.authservice.service.UserBusinessService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class UserBusinessServiceImpl implements UserBusinessService {
    private final UserServiceImpl userServiceImpl;
    private final ObjectMapper objectMapper;
    private final BCryptPasswordEncoder passwordEncoder;

    @Override
    public void register(RequestCreateUserDto requestCreateUserDto) {
        if (userServiceImpl.checkUsername(requestCreateUserDto.getUsername()))
            throw BaseException.of(ErrorEnum.USER_ALREADY_EXISTS);
        //TODO send email or other verification type to change user status (enable)
        userServiceImpl.insertOrUpdate(toForCustomer(requestCreateUserDto));
    }

    private UserEntity toForCustomer(RequestCreateUserDto requestCreateUserDto) {
        return UserEntity.builder()
                .fullName(requestCreateUserDto.getFullName())
                .username(requestCreateUserDto.getUsername())
                .password(passwordEncoder.encode(requestCreateUserDto.getPassword()))
                .roles(Collections.singleton(RoleEntity.builder().id(1L).build()))
                .status(UserStatusEnum.EMAIL_CONFIRMED) //TODO if email service applied turn this into CREATED
                .build();
    }

    @Override
    @SneakyThrows
    public String validate() {
        final JwtUserDetails userDetails = (JwtUserDetails) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();
        return objectMapper.writeValueAsString(
                CustomJwtUserDetailsFactory.of(
                        userDetails.getId(),
                        userDetails.getUsername(),
                        userDetails.getPassword(),
                        userDetails.getAuthorities()
                )
        );
    }
}