package sch.summer.domain.workout.repository;

import org.springframework.data.repository.CrudRepository;
import sch.summer.domain.member.Member;
import sch.summer.domain.workout.Workout;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface WorkoutRepository extends CrudRepository<Workout, Long> {

    List<Workout> findByMemberIdAndDateBetween(Long memberId, LocalDate startDate, LocalDate endDate);
    List<Workout> findByMemberIdAndDate(Long memberId, LocalDate date);

}
