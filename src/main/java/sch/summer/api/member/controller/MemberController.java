package sch.summer.api.member.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sch.summer.api.member.dto.MemberDto;
import sch.summer.api.member.service.MemberService;
import sch.summer.domain.member.Member;
import sch.summer.domain.member.repository.MemberRepository;
import sch.summer.resolver.CurrentMember;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/members")
public class MemberController {

    private final MemberRepository memberRepository;
    private final ModelMapper modelMapper;

    @GetMapping("/me")
    public ResponseEntity<MemberDto> getMemberInfo(
            @CurrentMember Member member
    ) {
        System.out.println("member = " + member.toString());
        MemberDto memberDto = modelMapper.map(member, MemberDto.class);
        return ResponseEntity.ok(memberDto);
    }
}
