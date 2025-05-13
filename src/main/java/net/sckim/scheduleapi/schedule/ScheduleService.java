package net.sckim.scheduleapi.schedule;

import net.sckim.scheduleapi.schedule.dto.CreateScheduleRequest;
import net.sckim.scheduleapi.schedule.dto.ScheduleResponse;

public interface ScheduleService {
    ScheduleResponse create(CreateScheduleRequest request);

    ScheduleResponse getSchedule(Long scheduleId);
}
