package az.et.authservice.controller;

import az.et.authservice.dto.request.LoginRequestDto;
import az.et.authservice.dto.request.RequestCreateUserDto;
import az.et.authservice.dto.response.BaseResponse;
import az.et.authservice.dto.response.ResponseTokenDto;
import az.et.authservice.service.AuthBusinessService;
import az.et.authservice.service.functional.UserBusinessService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthBusinessService authBusinessService;
    private final UserBusinessService userBusinessService;

    @PostMapping("/login")
    public ResponseEntity<BaseResponse<ResponseTokenDto>> login(@Valid @RequestBody LoginRequestDto loginrequestDto) {
        return ResponseEntity.ok(
                BaseResponse.ok(
                        authBusinessService.login(loginrequestDto)
                )
        );
    }

    @PostMapping("/register")
    public ResponseEntity<BaseResponse<Object>> register(@RequestBody RequestCreateUserDto createUserRequestDto) {
        userBusinessService.register(createUserRequestDto);
        return ResponseEntity.ok(
                BaseResponse.ok()
        );
    }

    @GetMapping("/validate")
    public String validate() {
        return userBusinessService.validate();
    }
}