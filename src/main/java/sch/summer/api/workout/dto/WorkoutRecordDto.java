package sch.summer.api.workout.dto;

import lombok.Getter;

import java.time.LocalDate;

@Getter
public class WorkoutRecordDto {

    private LocalDate date;
    private Long runningTime;
    private Long averageBPM;

}
