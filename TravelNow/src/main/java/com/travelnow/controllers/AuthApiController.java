package com.travelnow.controllers;

import javax.annotation.security.RolesAllowed;

import com.travelnow.core.security.JwtTokenUtil;
import com.travelnow.core.security.models.MyUserDetails;
import com.travelnow.models.auth.AuthRequest;
import com.travelnow.models.auth.CreateUserInfo;
import com.travelnow.models.auth.UserView;
import com.travelnow.services.UserService;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import com.travelnow.core.dbacces.models.security.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping(path = "api/auth/")
public class AuthApiController {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenUtil jwtTokenUtil;
    private final UserService userService;

    public AuthApiController(AuthenticationManager authenticationManager,
                             JwtTokenUtil jwtTokenUtil,
                             UserService userService){
        this.authenticationManager = authenticationManager;
        this.jwtTokenUtil = jwtTokenUtil;
        this.userService = userService;
    }

    @PostMapping("token")
    public ResponseEntity<UserView> login(@RequestBody AuthRequest request) {

        try {
            Authentication authenticate = authenticationManager
                .authenticate(
                    new UsernamePasswordAuthenticationToken(
                        request.getUsername(), request.getPassword()
                    )
                );

            MyUserDetails userDetails = (MyUserDetails) authenticate.getPrincipal();

            UserView userView = new UserView(
                userService.findUserByUsername(userDetails.getUsername())
            );

            String token = jwtTokenUtil.generateAccessToken(userDetails);

            return ResponseEntity.ok()
                .header(
                    HttpHeaders.AUTHORIZATION,
                    token
                )
                .body(userView);

        } catch (BadCredentialsException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }


    @PutMapping("add")
    public UserView createAccount(@RequestBody CreateUserInfo request) throws Exception {

        User user = userService.createAccount(request);
        return new UserView(user);
    }


    @GetMapping("/test")
    @RolesAllowed("ROLE_USER")
    public String test(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        MyUserDetails userDetails = (MyUserDetails) auth.getPrincipal();

        return "Test passed: " + userDetails.getUsername();
    }

}

// Based on
// https://github.com/Yoh0xFF/java-spring-security-example/blob/master/src/main/java/io/example/service/UserService.java
// https://www.toptal.com/spring/spring-security-tutorial
