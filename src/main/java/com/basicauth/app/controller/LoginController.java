package com.basicauth.app.controller;

import com.basicauth.app.dto.ReqRes;
import com.basicauth.app.entity.LoginCreds;
import com.basicauth.app.entity.UserProfile;
import com.basicauth.app.enums.Role;
import com.basicauth.app.repository.RegisterNewUserRepository;
import com.basicauth.app.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "*")
public class LoginController {


    @Autowired
    private AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<ReqRes> signUp(@RequestBody ReqRes signUpRequest){
        return ResponseEntity.ok(authService.signUp(signUpRequest));
    }
    @PostMapping("/signin")
    public ResponseEntity<ReqRes> signIn(@RequestBody ReqRes signInRequest){
        return ResponseEntity.ok(authService.signIn(signInRequest));
    }
    @PostMapping("/refresh")
    public ResponseEntity<ReqRes> refreshToken(@RequestBody ReqRes refreshTokenRequest){
        return ResponseEntity.ok(authService.refreshToken(refreshTokenRequest));
    }

    @GetMapping("/role/{role}")
    public List<UserProfile> getUsersByRole(@PathVariable Role role) {
        return authService.getUsersByRole(role);
    }

    @PutMapping("/role/{userId}")
    public UserProfile updateUserRole(@PathVariable Long userId, @RequestParam Role newRole) {
        return authService.updateUserRole(userId, newRole);
    }
}