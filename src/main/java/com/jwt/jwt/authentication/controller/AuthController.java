package com.jwt.jwt.authentication.controller;

import com.jwt.jwt.authentication.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(@RequestBody AuthRequestDTO authRequestDTO)
    {
        var jwtToken= authService.login(authRequestDTO.username(),authRequestDTO.password());

        var authResponseDTO= new AuthResponseDTO(jwtToken, AuthStatus.LOGIN_SUCCESS);

        return ResponseEntity.status(HttpStatus.OK).body(authResponseDTO);
    }

    @PostMapping("/sign-up")
    public ResponseEntity<AuthResponseDTO> signUp(@RequestBody AuthRequestDTO authRequestDTO)
    {
        try
        {
            var jwtToken= authService.signUp(authRequestDTO.name(),authRequestDTO.username(), authRequestDTO.password());
            var authResponseDTO= new AuthResponseDTO(jwtToken, AuthStatus.USER_CREATED_SUCCESSFULLY);

            return ResponseEntity.status(HttpStatus.OK).body(authResponseDTO);
        }
        catch (Exception e)
        {
            var jwtToken= authService.signUp(authRequestDTO.name(),authRequestDTO.username(), authRequestDTO.password());
            var authResponseDTO= new AuthResponseDTO(jwtToken, AuthStatus.USER_NOT_CREATED_SUCCESSFULLY);

            return ResponseEntity.status(HttpStatus.CONFLICT).body(authResponseDTO);
        }


    }
}
