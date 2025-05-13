package net.sckim.scheduleapi.schedule;

import net.sckim.scheduleapi.schedule.dto.CreateScheduleRequest;
import net.sckim.scheduleapi.schedule.dto.DeleteScheduleRequest;
import net.sckim.scheduleapi.schedule.dto.EditScheduleRequest;
import net.sckim.scheduleapi.schedule.dto.ScheduleResponse;
import net.sckim.scheduleapi.schedule.entity.Schedule;
import net.sckim.scheduleapi.user.UserRepository;
import net.sckim.scheduleapi.user.entity.User;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class ScheduleServiceImpl implements ScheduleService {
    private final ScheduleRepository scheduleRepository;
    private final UserRepository userRepository;

    public ScheduleServiceImpl(ScheduleRepository scheduleRepository, UserRepository userRepository) {
        this.scheduleRepository = scheduleRepository;
        this.userRepository = userRepository;
    }

    @Override
    public ScheduleResponse create(CreateScheduleRequest request) {
        final User user = userRepository.findById(request.getUserId())
                .orElseThrow();

        final LocalDateTime now = LocalDateTime.now();

        Schedule newSchedule = new Schedule(request.getUserId(), request.getContents(), request.getPassword(), now, now);
        newSchedule = scheduleRepository.save(newSchedule);

        return new ScheduleResponse(newSchedule, user);
    }

    @Override
    public ScheduleResponse getSchedule(Long scheduleId) {
        final Schedule schedule = scheduleRepository.findById(scheduleId)
                .orElseThrow();

        final User user = userRepository.findById(schedule.getUserId())
                .orElseThrow();

        return new ScheduleResponse(schedule, user);
    }

    @Override
    public List<ScheduleResponse> getScheduleList(LocalDate updatedDate, Long userId) {
        final List<Schedule> scheduleList = scheduleRepository.findAllBy(updatedDate, userId);

        User user;
        List<ScheduleResponse> list = new ArrayList<>();
        for (Schedule newSchedule : scheduleList) {
            user = userRepository.findById(newSchedule.getUserId())
                    .orElseThrow();
            ScheduleResponse scheduleResponse = new ScheduleResponse(newSchedule, user);
            list.add(scheduleResponse);
        }

        return list;
    }

    @Override
    public ScheduleResponse editSchedule(Long scheduleId, EditScheduleRequest editRequest) {
        final Schedule schedule = scheduleRepository.findById(scheduleId).orElseThrow();

        // 비밀번호 췍
        if (!schedule.getPassword().equals(editRequest.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않음");
        }

        // 일정 내용 변경
        schedule.edit(editRequest.getContents());
        scheduleRepository.update(schedule);

        // 유저 이름 변경
        final User user = userRepository.findById(schedule.getUserId())
                        .orElseThrow();
        user.editName(editRequest.getName());
        userRepository.update(user);

        return new ScheduleResponse(schedule, user);
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