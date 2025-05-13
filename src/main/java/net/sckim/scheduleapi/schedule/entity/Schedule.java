package net.sckim.scheduleapi.schedule.entity;

import lombok.Getter;
import org.apache.logging.log4j.util.Strings;

import java.time.LocalDateTime;

@Getter
public class Schedule {
    private Long id;
    private Long userId;
    private String contents;
    private String password;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Schedule(Long userId, String contents, String password, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.userId = userId;
        this.contents = contents;
        this.password = password;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void edit(String contents) {
        if (Strings.isNotBlank(contents)) {
            this.contents = contents;
        }
    }
}
