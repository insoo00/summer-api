package sch.summer.api.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import sch.summer.api.member.dto.MemberDto;

@Getter
@AllArgsConstructor
public class LoginResponseDto {

    private MemberDto member;
    private String accessToken;

}
