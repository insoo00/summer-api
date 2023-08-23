package sch.summer.api.auth.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.modelmapper.ModelMapper;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sch.summer.api.auth.dto.LoginRequestDto;
import sch.summer.api.auth.dto.LoginResponseDto;
import sch.summer.api.member.dto.MemberDto;
import sch.summer.domain.accessToken.AccessToken;
import sch.summer.domain.accessToken.constant.AuthType;
import sch.summer.domain.accessToken.repository.AccessTokenRepository;
import sch.summer.domain.member.Member;
import sch.summer.domain.member.repository.MemberRepository;
import sch.summer.domain.member.service.MemberService;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDate;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AuthService {

    private final MemberRepository memberRepository;
    private final AccessTokenRepository accessTokenRepository;
    private final MemberService memberService;
    private final ModelMapper modelMapper;

    @Transactional
    public LoginResponseDto authenticateWithKakao(LoginRequestDto loginRequestDto){

        String kakaoToken = loginRequestDto.getOauth().getToken();
        Long kakaoId = getKakaoId(kakaoToken);
        LocalDate birth = loginRequestDto.getBirth();

        Member member = memberService.findOrCreateMemberByKakaoId(kakaoId, birth);
        MemberDto memberDto = modelMapper.map(member, MemberDto.class);

        String accessToken = issueAccessToken(memberDto.getId(), AuthType.KAKAO);

        return new LoginResponseDto(memberDto, accessToken);
    }

    private static Long getKakaoId(String kakaoToken) {

        String reqURL = "https://kapi.kakao.com/v2/user/me";
        Long kakaoId = null;
        try {
            URL url = new URL(reqURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setRequestMethod("GET");
            conn.setDoOutput(true);
            conn.setRequestProperty("Authorization", "Bearer " + kakaoToken);

            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line = "";
            String result = "";

            while ((line = br.readLine()) != null) {
                result += line;
            }

            ObjectMapper objectMapper = new ObjectMapper();
            Map<String, Object> jsonMap = objectMapper.readValue(result, new TypeReference<Map<String, Object>>() {});
             kakaoId= (Long) jsonMap.get("id");

        } catch (IOException e) {
            e.printStackTrace();
        }
        return kakaoId;
    }

    @Transactional
    public String issueAccessToken(Long memberId, AuthType authType) {
        Optional<Member> member = memberRepository.findById(memberId);
        String token = null;

        if (member.isPresent()) {
            Member findMember = member.get();
            int retryCount = 0;
            int maxRetryCount = 10;

            while(++retryCount <= maxRetryCount) {
                try {
                    token = RandomStringUtils.randomAlphanumeric(32);
                    AccessToken accessToken = AccessToken.builder()
                            .member(findMember)
                            .authType(authType)
                            .token(token)
                            .build();
                    accessTokenRepository.save(accessToken);
                    break;
                } catch (DataIntegrityViolationException e) {
                    //TODO: exception을 catch 할 수 없음. controller에서 예외처리 필요
                    //TODO: 생성된 token이 현재 DB에 있는지 체크하는 메소드를 사용하는 것이 좋을 것 같음. (이는 동시성 문제가 내재되어있음)
                    log.info("Token is not unique. retryCount = {}", retryCount);
                }
            }
        }
        return token;
    }

    //kakao 인증: code -> accessToken 받아오기
    public String getKakaoAccessToken(String code) {
        String client_id = "07422eb6ce33c33b2e481099f4f052aa";
        String redirectUrl = "http://localhost:8080/authenticate/kakao/web";
        String access_Token = "";
        String reqURL = "https://kauth.kakao.com/oauth/token";

        try {
            URL url = new URL(reqURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setRequestMethod("POST");
            conn.setDoOutput(true);

            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));
            StringBuilder sb = new StringBuilder();
            sb.append("grant_type=authorization_code");
            sb.append("&client_id=" + client_id);
            sb.append("&redirect_uri=" + redirectUrl);
            sb.append("&code=" + code);
            bw.write(sb.toString());
            bw.flush();

            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line = "";
            String result = "";

            while ((line = br.readLine()) != null) {
                result += line;
            }
            System.out.println("response body : " + result);

            // jackson objectmapper 객체 생성
            ObjectMapper objectMapper = new ObjectMapper();
            // JSON String -> Map
            Map<String, Object> jsonMap = objectMapper.readValue(result, new TypeReference<Map<String, Object>>() {
            });
            access_Token = jsonMap.get("access_token").toString();

            br.close();
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return access_Token;
    }
}
