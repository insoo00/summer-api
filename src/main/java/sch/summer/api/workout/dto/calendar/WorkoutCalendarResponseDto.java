package sch.summer.api.workout.dto.calendar;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class WorkoutCalendarResponseDto {

    private List<WorkoutCalendarDto> WorkoutCalendar;

}
