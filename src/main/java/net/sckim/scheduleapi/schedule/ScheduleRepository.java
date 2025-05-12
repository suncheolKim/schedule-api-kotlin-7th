package net.sckim.scheduleapi.schedule;


import net.sckim.scheduleapi.schedule.entity.Schedule;

public interface ScheduleRepository {
    Schedule save(Schedule schedule);
}
