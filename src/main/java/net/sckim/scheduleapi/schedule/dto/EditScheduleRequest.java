package net.sckim.scheduleapi.schedule.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class EditScheduleRequest {
    private String contents;
    private String name;

    @NotBlank
    private String password;
}
