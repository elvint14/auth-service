package az.et.authservice.controller;

import az.et.authservice.security.JwtUserDetails;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/test")
public class TestController {

    @GetMapping
    public String test() {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        System.out.println((JwtUserDetails) authentication.getPrincipal());
        System.out.println(authentication.getAuthorities().toString());
        return "ok";
    }

}