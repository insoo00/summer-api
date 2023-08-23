package sch.summer.domain.workout;

import lombok.*;
import sch.summer.domain.member.Member;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "workout")
@Builder @Getter @ToString
@AllArgsConstructor @NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Workout {

    @Id @GeneratedValue
    @Column(name = "workout_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Column(name = "running_time", nullable = false)
    private Long runningTime;

    @Column(name = "running_BPM", nullable = false)
    private Long averageBPM;

    @Column(nullable = false)
    private LocalDate date;
}
