package sch.summer.api.workout.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sch.summer.api.workout.dto.WorkoutRecordDto;
import sch.summer.domain.member.Member;
import sch.summer.domain.member.repository.MemberRepository;
import sch.summer.domain.workout.Workout;
import sch.summer.domain.workout.repository.WorkoutRepository;

import java.util.Optional;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class WorkoutService {

    private final MemberRepository memberRepository;
    private final WorkoutRepository workoutRepository;

    @Transactional
    public Boolean saveRecord(Long memberId, WorkoutRecordDto workoutRecordDto) {
        Optional<Member> member = memberRepository.findById(memberId);
        if(member.isPresent()) {
            Member findMember = member.get();
            Workout workoutRecord = Workout.builder()
                    .member(findMember)
                    .runningTime(workoutRecordDto.getRunningTime())
                    .averageBPM(workoutRecordDto.getAverageBPM())
                    .date(workoutRecordDto.getDate())
                    .build();
            workoutRepository.save(workoutRecord);
            return true;
        }
        return false;
    }

}
