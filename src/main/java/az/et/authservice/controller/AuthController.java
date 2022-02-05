package az.et.authservice.controller;

import az.et.authservice.dto.request.LoginRequestDto;
import az.et.authservice.dto.request.RequestCreateUserDto;
import az.et.authservice.dto.response.BaseResponse;
import az.et.authservice.dto.response.ResponseTokenDto;
import az.et.authservice.service.AuthBusinessService;
import az.et.authservice.service.UserBusinessService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Api(
        value = "AuthController",
        tags = " "
)
public class AuthController {
    private final AuthBusinessService authBusinessService;
    private final UserBusinessService userBusinessService;

    @PostMapping("/login")
    @ApiOperation(
            value = "Login",
            tags = " "
    )
    @ApiResponses({
            @ApiResponse(code = 200, message = "SUCCESS")
    })
    public ResponseEntity<BaseResponse<ResponseTokenDto>> login(@Valid @RequestBody LoginRequestDto loginrequestDto) {
        return ResponseEntity.ok(
                BaseResponse.ok(
                        authBusinessService.login(loginrequestDto)
                )
        );
    }

    @PostMapping("/register")
    @ApiOperation(
            value = "Register",
            tags = " "
    )
    @ApiResponses({
            @ApiResponse(code = 200, message = "SUCCESS")
    })
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