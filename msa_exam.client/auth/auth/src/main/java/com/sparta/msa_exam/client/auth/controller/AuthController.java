package com.sparta.msa_exam.client.auth.controller;

import com.sparta.msa_exam.client.auth.dto.AuthResponse;
import com.sparta.msa_exam.client.auth.dto.SignupRequestDto;
import com.sparta.msa_exam.client.auth.dto.signInRequestDto;
import com.sparta.msa_exam.client.auth.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    /**
     * 사용자 ID를 받아 JWT 액세스 토큰을 생성하여 응답합니다.
     *
     * @param user_id 사용자 ID
     * @return JWT 액세스 토큰을 포함한 AuthResponse 객체를 반환합니다.
     */
    @PostMapping("/auth/signIn")
    public ResponseEntity<?> createAuthenticationToken(@RequestBody signInRequestDto requestDto){


        return ResponseEntity.ok(new AuthResponse(authService.createAccessToken(requestDto)));
    }

    @PostMapping("/auth/signUp")
    public ResponseEntity<?> signUp(@RequestBody SignupRequestDto requestDto){

        authService.signUp(requestDto);

        return ResponseEntity.ok().build();
    }
}