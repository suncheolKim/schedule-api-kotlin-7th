package net.sckim.scheduleapi.schedule;

import net.sckim.scheduleapi.schedule.dto.CreateScheduleRequest;
import net.sckim.scheduleapi.schedule.dto.EditScheduleRequest;
import net.sckim.scheduleapi.schedule.dto.ScheduleResponse;

import java.time.LocalDate;
import java.util.List;

public interface ScheduleService {
    ScheduleResponse create(CreateScheduleRequest request);

    ScheduleResponse getSchedule(Long scheduleId);

    List<ScheduleResponse> getScheduleList(LocalDate updatedDate, String writer);

    ScheduleResponse editSchedule(Long scheduleId, EditScheduleRequest editRequest);
}
