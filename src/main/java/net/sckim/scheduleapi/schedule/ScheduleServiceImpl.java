package net.sckim.scheduleapi.schedule;

import net.sckim.scheduleapi.schedule.dto.CreateScheduleRequest;
import net.sckim.scheduleapi.schedule.dto.ScheduleResponse;
import net.sckim.scheduleapi.schedule.entity.Schedule;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class ScheduleServiceImpl implements ScheduleService {
    private final ScheduleRepository scheduleRepository;

    public ScheduleServiceImpl(ScheduleRepository scheduleRepository) {
        this.scheduleRepository = scheduleRepository;
    }

    @Override
    public ScheduleResponse create(CreateScheduleRequest request) {
        final LocalDateTime now = LocalDateTime.now();

        Schedule newSchedule = new Schedule(request.getContents(), request.getName(), request.getPassword(), now, now);
        newSchedule = scheduleRepository.save(newSchedule);

        return new ScheduleResponse(newSchedule);
    }

    @Override
    public ScheduleResponse getSchedule(Long scheduleId) {
        final Schedule schedule = scheduleRepository.findById(scheduleId)
                .orElseThrow();

        return new ScheduleResponse(schedule);
    }
}