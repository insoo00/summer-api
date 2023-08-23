package sch.summer.api.auth.dto;

import lombok.Getter;
import sch.summer.domain.accessToken.constant.AuthType;

@Getter
public class OauthDto {

    private String token;

}
