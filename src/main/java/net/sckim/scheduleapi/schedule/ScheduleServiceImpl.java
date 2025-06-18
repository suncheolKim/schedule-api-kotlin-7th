package net.sckim.scheduleapi.schedule;

import net.sckim.scheduleapi.exception.EntityNotFoundException;
import net.sckim.scheduleapi.exception.PasswordMismatchedException;
import net.sckim.scheduleapi.schedule.dto.CreateScheduleRequest;
import net.sckim.scheduleapi.schedule.dto.DeleteScheduleRequest;
import net.sckim.scheduleapi.schedule.dto.EditScheduleRequest;
import net.sckim.scheduleapi.schedule.dto.ScheduleResponse;
import net.sckim.scheduleapi.schedule.entity.Schedule;
import net.sckim.scheduleapi.user.UserRepository;
import net.sckim.scheduleapi.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
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
                .orElseThrow(() -> new EntityNotFoundException(request.getUserId(), User.class));

        final LocalDateTime now = LocalDateTime.now();

        Schedule newSchedule = new Schedule(request.getUserId(), request.getContents(), request.getPassword(), now, now);
        newSchedule = scheduleRepository.save(newSchedule);

        return new ScheduleResponse(newSchedule, user);
    }

    @Override
    public ScheduleResponse getSchedule(Long scheduleId) {
        final Schedule schedule = scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new EntityNotFoundException(scheduleId, Schedule.class));

        final User user = userRepository.findById(schedule.getUserId())
                .orElseThrow(() -> new EntityNotFoundException(schedule.getUserId(), User.class));

        return new ScheduleResponse(schedule, user);
    }

    @Override
    public List<ScheduleResponse> getScheduleList(LocalDate updatedDate, Long userId) {
        final List<Schedule> scheduleList = scheduleRepository.findAllBy(updatedDate, userId);

        User user;
        List<ScheduleResponse> list = new ArrayList<>();
        for (Schedule newSchedule : scheduleList) {
            user = userRepository.findById(newSchedule.getUserId())
                    .orElseThrow(() -> new EntityNotFoundException(newSchedule.getUserId(), User.class));
            ScheduleResponse scheduleResponse = new ScheduleResponse(newSchedule, user);
            list.add(scheduleResponse);
        }

        return list;
    }

    @Override
    public Page<ScheduleResponse> getSchedulePage(LocalDate updatedDate, Long userId, Integer page, Integer size) {
        final List<Schedule> scheduleList = scheduleRepository.findPageBy(updatedDate, userId, page, size);
        final long totalCount = scheduleRepository.countAllBy(updatedDate, userId);

        User user;
        final List<ScheduleResponse> responseResult = new ArrayList<>();
        for (Schedule newSchedule : scheduleList) {
            user = userRepository.findById(newSchedule.getUserId())
                    .orElseThrow(() -> new EntityNotFoundException(newSchedule.getUserId(), User.class));
            ScheduleResponse scheduleResponse = new ScheduleResponse(newSchedule, user);
            responseResult.add(scheduleResponse);
        }

        // PageImpl의 내부 페이지 시작 번호는 0번부터 시작한다. 외부에서 요청할 때 시작 페이지가 1이므로 -1을 해줘야 한다.
        return new PageImpl<>(responseResult, PageRequest.of(page - 1, size), totalCount);
    }

    @Override
    public ScheduleResponse editSchedule(Long scheduleId, EditScheduleRequest editRequest) {
        final Schedule schedule = scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new EntityNotFoundException(scheduleId, Schedule.class));

        // 비밀번호 췍
        if (!schedule.getPassword().equals(editRequest.getPassword())) {
            throw new PasswordMismatchedException();
        }

        // 일정 내용 변경
        schedule.edit(editRequest.getContents());
        scheduleRepository.update(schedule);

        // 유저 이름 변경
        final User user = userRepository.findById(schedule.getUserId())
                        .orElseThrow(() -> new EntityNotFoundException(schedule.getUserId(), User.class));
        user.editName(editRequest.getName());
        userRepository.update(user);

        return new ScheduleResponse(schedule, user);
    }

    @Override
    public void deleteSchedule(Long scheduleId, DeleteScheduleRequest deleteRequest) {
        final Schedule schedule = scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new EntityNotFoundException(scheduleId, Schedule.class));

        if (!schedule.getPassword().equals(deleteRequest.getPassword())) {
            throw new PasswordMismatchedException();
        }

        scheduleRepository.deleteById(scheduleId);
    }
}