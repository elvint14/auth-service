package az.et.authservice.service;

import az.et.authservice.dto.request.RequestCreateUserDto;
import org.springframework.stereotype.Service;

@Service
public interface UserBusinessService {

    void register(RequestCreateUserDto requestCreateUserDto);

}
