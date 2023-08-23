package sch.summer.domain.member;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "member")
@Builder @Getter @ToString
@AllArgsConstructor @NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member {

    @Id @GeneratedValue
    @Column(name = "member_id")
    private Long id;

    @Column(name = "kakao_id", unique = true, nullable = false)
    private Long kakaoId;

    @Column(nullable = false)
    private LocalDate birth;

}
