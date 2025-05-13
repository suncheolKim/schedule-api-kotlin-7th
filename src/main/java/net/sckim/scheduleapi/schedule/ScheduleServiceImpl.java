package net.sckim.scheduleapi.schedule;

import net.sckim.scheduleapi.schedule.dto.CreateScheduleRequest;
import net.sckim.scheduleapi.schedule.dto.DeleteScheduleRequest;
import net.sckim.scheduleapi.schedule.dto.EditScheduleRequest;
import net.sckim.scheduleapi.schedule.dto.ScheduleResponse;
import net.sckim.scheduleapi.schedule.entity.Schedule;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

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

    @Override
    public List<ScheduleResponse> getScheduleList(LocalDate updatedDate, String writer) {
        final List<Schedule> scheduleList = scheduleRepository.findAllBy(updatedDate, writer);

        return scheduleList.stream()
                .map(ScheduleResponse::new)
                .toList();
    }

    @Override
    public ScheduleResponse editSchedule(Long scheduleId, EditScheduleRequest editRequest) {
        final Schedule schedule = scheduleRepository.findById(scheduleId).orElseThrow();

        if (!schedule.getPassword().equals(editRequest.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않음");
        }

        schedule.edit(editRequest);

        scheduleRepository.update(schedule);

        return new ScheduleResponse(schedule);
    }

    @Override
    public void deleteSchedule(Long scheduleId, DeleteScheduleRequest deleteRequest) {
        final Schedule schedule = scheduleRepository.findById(scheduleId).orElseThrow();

        if (!schedule.getPassword().equals(deleteRequest.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않음");
        }

        scheduleRepository.deleteById(scheduleId);
    }
}