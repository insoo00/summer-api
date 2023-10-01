package sch.summer.resolver;

import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import sch.summer.domain.accessToken.AccessToken;
import sch.summer.domain.accessToken.repository.AccessTokenRepository;
import sch.summer.domain.member.Member;
import sch.summer.domain.member.repository.MemberRepository;

import javax.servlet.http.HttpServletRequest;
import java.nio.file.AccessDeniedException;

@Slf4j
@Component
public class CurrentMemberArgResolver implements HandlerMethodArgumentResolver {

    @Autowired private AccessTokenRepository accessTokenRepository;

    public CurrentMemberArgResolver() {
    }


    @Override
    public boolean supportsParameter(MethodParameter parameter) {

        boolean hasEmailAnnotation = parameter.hasParameterAnnotation(CurrentMember.class);
        boolean hasMemberType = Member.class.isAssignableFrom(parameter.getParameterType());

        return hasEmailAnnotation && hasMemberType;
    }

    @Transactional
    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();
        String authorization = request.getHeader(HttpHeaders.AUTHORIZATION);
        String token = "";
        if (authorization != null && authorization.startsWith("Bearer ")) {
            token = authorization.substring(7);
        }
        AccessToken findMemberByAccessToken = accessTokenRepository.findByToken(token).orElseThrow(() -> new AccessDeniedException("Access Denied"));
        Member findMember = findMemberByAccessToken.getMember();
        return findMember;
    }
}
