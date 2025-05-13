package net.sckim.scheduleapi.schedule.entity;

import lombok.Getter;
import net.sckim.scheduleapi.schedule.dto.EditScheduleRequest;
import org.apache.logging.log4j.util.Strings;

import java.time.LocalDateTime;

@Getter
public class Schedule {
    private Long id;
    private String contents;
    private String name;
    private String password;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Schedule(String contents, String name, String password, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.contents = contents;
        this.name = name;
        this.password = password;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void edit(EditScheduleRequest editRequest) {
        if (Strings.isNotBlank(editRequest.getName())) {
            this.name = editRequest.getName();
        }

        if (Strings.isNotBlank(editRequest.getContents())) {
            this.contents = editRequest.getContents();
        }
    }
}
