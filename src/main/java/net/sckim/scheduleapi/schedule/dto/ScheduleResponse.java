package net.sckim.scheduleapi.schedule.dto;

import lombok.Getter;
import net.sckim.scheduleapi.schedule.entity.Schedule;
import net.sckim.scheduleapi.user.entity.User;

import java.time.LocalDateTime;

@Getter
public class ScheduleResponse {
    private Long id;
    private Long userId;
    private String name;
    private String email;
    private String contents;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public ScheduleResponse(Schedule newSchedule, User user) {
        this.id = newSchedule.getId();
        this.contents = newSchedule.getContents();
        this.userId = user.getId();
        this.name = user.getName();
        this.email = user.getEmail();
        this.createdAt = newSchedule.getCreatedAt();
        this.updatedAt = newSchedule.getUpdatedAt();
    }
}
