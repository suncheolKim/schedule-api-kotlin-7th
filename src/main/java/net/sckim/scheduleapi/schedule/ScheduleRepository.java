package net.sckim.scheduleapi.schedule;


import net.sckim.scheduleapi.schedule.entity.Schedule;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ScheduleRepository {
    Schedule save(Schedule schedule);

    Optional<Schedule> findById(Long scheduleId);

    List<Schedule> findAllBy(LocalDate updatedDate, String writer);

    int update(Schedule schedule);

    int deleteById(Long scheduleId);
}
