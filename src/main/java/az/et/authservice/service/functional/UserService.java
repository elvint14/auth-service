package az.et.authservice.service.functional;

import az.et.authservice.entity.UserEntity;
import az.et.authservice.constant.ErrorEnum;
import az.et.authservice.exception.BaseException;
import az.et.authservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public UserEntity findByUsernameOrThrowEx(String username, ErrorEnum error){
        return userRepository.findByUsername(username)
                .orElseThrow(() -> BaseException.of(error));
    }

    public boolean checkUsername(String username){
        return userRepository.existsByUsername(username);
    }

    public void insertOrUpdate(UserEntity userEntity){
        userRepository.save(userEntity);
    }

}
