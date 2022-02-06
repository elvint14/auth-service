package az.et.authservice.service;

import az.et.authservice.constant.ErrorEnum;
import az.et.authservice.entity.UserEntity;
import org.springframework.stereotype.Service;

@Service
public interface UserService {

    UserEntity findByUsernameOrThrowEx(String username, ErrorEnum error);

    boolean checkUsername(String username);

    void insertOrUpdate(UserEntity userEntity);

}
