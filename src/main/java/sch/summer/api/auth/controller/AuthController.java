package sch.summer.api.auth.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sch.summer.api.auth.dto.LoginRequestDto;
import sch.summer.api.auth.dto.LoginResponseDto;
import sch.summer.api.auth.service.AuthService;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/authenticate")
public class AuthController {

    private final AuthService authService;

    //앱
    @PostMapping("/kakao")
    public ResponseEntity<LoginResponseDto> authenticateWithKakao(
            @RequestBody LoginRequestDto loginRequestDto
    ) {
        LoginResponseDto loginResponseDto = authService.authenticateWithKakao(loginRequestDto);
        return ResponseEntity.ok(loginResponseDto);
    }

    //웹
    @GetMapping("/kakao/web")
    public void getKakaoAccessToken(@RequestParam String code) {
        String kakaoAccessToken = authService.getKakaoAccessToken(code);
        log.info("kakaoAccessToken = {}", kakaoAccessToken);
    }

}
