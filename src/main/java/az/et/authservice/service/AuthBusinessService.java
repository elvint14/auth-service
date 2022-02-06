package az.et.authservice.service;

import az.et.authservice.dto.request.LoginRequestDto;
import az.et.authservice.dto.response.ResponseTokenDto;
import org.springframework.stereotype.Service;

@Service
public interface AuthBusinessService {

    ResponseTokenDto login(LoginRequestDto loginDto);

}
