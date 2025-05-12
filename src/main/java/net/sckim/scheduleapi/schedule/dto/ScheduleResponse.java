package net.sckim.scheduleapi.schedule.dto;

import lombok.Getter;
import net.sckim.scheduleapi.schedule.entity.Schedule;

import java.time.LocalDateTime;

@Getter
public class ScheduleResponse {
    private Long id;
    private String contents;
    private String name;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public ScheduleResponse(Schedule newSchedule) {
        this.id = newSchedule.getId();
        this.contents = newSchedule.getContents();
        this.name = newSchedule.getName();
        this.createdAt = newSchedule.getCreatedAt();
        this.updatedAt = newSchedule.getUpdatedAt();
    }
}
