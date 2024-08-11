package com.sparta.msa_exam.client.auth.dto;

import lombok.Data;

@Data
public class signInRequestDto {
    private String username;
    private String password;
}
