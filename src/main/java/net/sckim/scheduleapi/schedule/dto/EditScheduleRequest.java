package net.sckim.scheduleapi.schedule.dto;

import lombok.Getter;

@Getter
public class EditScheduleRequest {
    private String contents;
    private String name;
    private String password;
}
