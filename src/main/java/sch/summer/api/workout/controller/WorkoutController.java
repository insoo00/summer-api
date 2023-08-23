package sch.summer.api.workout.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sch.summer.api.dto.Result;
import sch.summer.api.member.dto.MemberDto;
import sch.summer.api.workout.dto.calendar.WorkoutCalendarDto;
import sch.summer.api.workout.dto.WorkoutRecordDto;
import sch.summer.api.workout.dto.calendar.WorkoutCalendarResponseDto;
import sch.summer.api.workout.service.WorkoutService;
import sch.summer.domain.member.Member;
import sch.summer.domain.workout.Workout;
import sch.summer.domain.workout.repository.WorkoutRepository;
import sch.summer.resolver.CurrentMember;

import java.time.LocalDate;
import java.util.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/workout")
public class WorkoutController {

    private final WorkoutService workoutService;
    private final WorkoutRepository workoutRepository;
    private final ModelMapper modelMapper;

    @PostMapping("/record")
    public ResponseEntity<Result> recodeWorkout(
            @CurrentMember Member member,
            @RequestBody WorkoutRecordDto workoutRecordDto) {
        if (workoutService.saveRecord(member.getId(), workoutRecordDto)) {
            Result result = Result.createOk();
            return ResponseEntity.ok(result);
        }
        Result result = Result.createFail();
        return ResponseEntity.ok(result);
    }

    @GetMapping("/calendar")
    public ResponseEntity<WorkoutCalendarResponseDto> getWorkoutCalendar(
            @CurrentMember Member member,
            @RequestParam("date") String date) {

        LocalDate startDate = LocalDate.parse(date);
        LocalDate endDate = startDate.plusMonths(1);

        List<Workout> byMemberIdAndDateBetween = workoutRepository.findByMemberIdAndDateBetween(member.getId(), startDate, endDate);

        Map<LocalDate, List<sch.summer.api.workout.dto.calendar.WorkoutRecordDto>> workoutCalendarDto = new HashMap<>();
        for (Workout workout : byMemberIdAndDateBetween) {
            LocalDate workoutDate = workout.getDate();

            if (!workoutCalendarDto.containsKey(workoutDate)) {
                workoutCalendarDto.put(workoutDate, new ArrayList<>());
            }
            workoutCalendarDto.get(workoutDate).add(modelMapper.map(workout, sch.summer.api.workout.dto.calendar.WorkoutRecordDto.class));
        }

        Set<LocalDate> keys = workoutCalendarDto.keySet();
        List<WorkoutCalendarDto> finalTest = new ArrayList<>();
        for (LocalDate key : keys) {
            finalTest.add(new WorkoutCalendarDto(key.toString(), workoutCalendarDto.get(key)));
        }


        return ResponseEntity.ok(new WorkoutCalendarResponseDto(finalTest));
    }
}
