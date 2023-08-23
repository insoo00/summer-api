package sch.summer.api.auth.dto;

import lombok.Getter;

import java.time.LocalDate;

@Getter
public class LoginRequestDto {

    private OauthDto oauth;
    private LocalDate birth;

}
