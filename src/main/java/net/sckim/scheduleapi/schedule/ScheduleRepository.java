package net.sckim.scheduleapi.schedule;


import net.sckim.scheduleapi.schedule.entity.Schedule;

import java.util.Optional;

public interface ScheduleRepository {
    Schedule save(Schedule schedule);

    Optional<Schedule> findById(Long scheduleId);
}
