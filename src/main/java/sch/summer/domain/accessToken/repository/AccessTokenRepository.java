package sch.summer.domain.accessToken.repository;

import org.springframework.data.repository.CrudRepository;
import sch.summer.domain.accessToken.AccessToken;
import sch.summer.domain.member.Member;

import java.util.Optional;

public interface AccessTokenRepository extends CrudRepository<AccessToken, Long> {

    Optional<AccessToken> findByToken(String token);

}
