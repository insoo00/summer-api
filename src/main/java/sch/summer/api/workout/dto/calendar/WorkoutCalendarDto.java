package sch.summer.api.workout.dto.calendar;

import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
public class WorkoutCalendarDto {

    private String date;
    private List<WorkoutRecordDto> workoutRecords;

}
