package sch.summer.domain.member.repository;

import org.springframework.data.repository.CrudRepository;
import sch.summer.domain.member.Member;

import java.util.Optional;

public interface MemberRepository extends CrudRepository<Member, Long> {

    Optional<Member> findByKakaoId(Long kakaoId);

}
