package sch.summer.domain.accessToken;

import lombok.*;
import sch.summer.domain.accessToken.constant.AuthType;
import sch.summer.domain.member.Member;

import javax.persistence.*;

@Entity
@Table(name = "access_token")
@Builder @Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AccessToken {

    @Id @GeneratedValue
    @Column(name = "access_token_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Enumerated(EnumType.STRING)
    private AuthType authType;

    @Column(unique = true, nullable = false)
    private String token;

    //createdAt
    // https://wonit.tistory.com/484
}
