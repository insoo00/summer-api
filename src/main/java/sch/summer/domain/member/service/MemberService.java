package sch.summer.domain.member.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sch.summer.domain.member.Member;
import sch.summer.domain.member.repository.MemberRepository;

import java.time.LocalDate;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    @Transactional
    public Member findOrCreateMemberByKakaoId(Long kakaoId, LocalDate birth) {

        Optional<Member> findMember = memberRepository.findByKakaoId(kakaoId);

        //로그인
        if (findMember.isPresent()) {
            return findMember.get();
        }

        //회원가입
        Member member = Member.builder()
                .kakaoId(kakaoId)
                .birth(birth)
                .build();
        return memberRepository.save(member);
    }
}
