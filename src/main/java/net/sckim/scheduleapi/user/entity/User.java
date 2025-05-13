package net.sckim.scheduleapi.user.entity;

import lombok.Getter;
import org.apache.logging.log4j.util.Strings;

import java.time.LocalDateTime;

@Getter
public class User {
    private Long id;
    private String name;
    private String email;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public User(String name, String email, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.name = name;
        this.email = email;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void edit(String name) {
        if (Strings.isNotBlank(name)) {
            this.name = name;
        }
    }

    public void editName(String name) {
        if (Strings.isNotBlank(name)) {
            this.name = name;
        }
    }
}
