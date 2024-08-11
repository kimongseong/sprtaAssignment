package com.sparta.msa_exam.client.auth.service;

import com.sparta.msa_exam.client.auth.domain.User;
import com.sparta.msa_exam.client.auth.domain.UserRoleEnum;
import com.sparta.msa_exam.client.auth.dto.SignupRequestDto;
import com.sparta.msa_exam.client.auth.dto.signInRequestDto;
import com.sparta.msa_exam.client.auth.repository.AuthRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Optional;

@Service
public class AuthService {

    @Value("${spring.application.name}")
    private String issuer;

    @Value("${service.jwt.access-expiration}")
    private Long accessExpiration;

    private final SecretKey secretKey;



    private final PasswordEncoder passwordEncoder;
    private final AuthRepository authRepository;

    /**
     * AuthService 생성자.
     * Base64 URL 인코딩된 비밀 키를 디코딩하여 HMAC-SHA 알고리즘에 적합한 SecretKey 객체를 생성합니다.
     *
     * @param secretKey Base64 URL 인코딩된 비밀 키
     */
    public AuthService(@Value("${service.jwt.secret-key}") String secretKey, PasswordEncoder passwordEncoder, AuthRepository authRepository) {
        this.secretKey = new SecretKeySpec(secretKey.getBytes(StandardCharsets.UTF_8), Jwts.SIG.HS512.key().build().getAlgorithm());
        this.passwordEncoder = passwordEncoder;
        this.authRepository = authRepository;
    }


    public String createAccessToken(@RequestBody signInRequestDto requestDto) {

        String username = requestDto.getUsername();
        String password = requestDto.getPassword();

        User user = authRepository.findByUsername(username).orElseThrow(()->new IllegalArgumentException("User not found"));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new IllegalArgumentException("Incorrect password for user: " + username);
        }

        return Jwts.builder()
                // 사용자 ID를 클레임으로 설정
                .claim("user_id", user.getId())
                .claim("role", user.getRole())
                .claim("username", username)
                .claim("email", user.getEmail())
                // JWT 발행자를 설정
                .issuer(issuer)
                // JWT 발행 시간을 현재 시간으로 설정
                .issuedAt(new Date(System.currentTimeMillis()))
                // JWT 만료 시간을 설정
                .expiration(new Date(System.currentTimeMillis() + accessExpiration))
                // SecretKey를 사용하여 HMAC-SHA512 알고리즘으로 서명
                .signWith(secretKey)
                // JWT 문자열로 컴팩트하게 변환
                .compact();
    }


    public void signUp(@RequestBody SignupRequestDto requestDto) {

        String username = requestDto.getUsername();
        String email = requestDto.getEmail();
        UserRoleEnum role = UserRoleEnum.USER;
        String password = passwordEncoder.encode(requestDto.getPassword());

        System.out.println(requestDto.getPassword());

       Optional<User> optionalUsername = authRepository.findByUsername(username);

        if (optionalUsername.isPresent()) {
            throw new IllegalArgumentException("Username already exists");
        }

        Optional<User> optionalEmail = authRepository.findByEmail(email);

        if (optionalEmail.isPresent()) {
            throw new IllegalArgumentException("Email already exists");
        }


         User user = new User(username, password, email, role);


        authRepository.save(user);

    }
}