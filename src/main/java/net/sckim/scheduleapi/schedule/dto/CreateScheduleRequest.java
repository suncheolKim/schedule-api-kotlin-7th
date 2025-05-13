package net.sckim.scheduleapi.schedule.dto;

import lombok.Getter;

@Getter
public class CreateScheduleRequest {
    private Long userId;
    private String contents;
    private String password;
}
