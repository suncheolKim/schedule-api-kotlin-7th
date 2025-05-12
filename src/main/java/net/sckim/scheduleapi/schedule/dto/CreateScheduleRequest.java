package net.sckim.scheduleapi.schedule.dto;

import lombok.Getter;

@Getter
public class CreateScheduleRequest {
    private String contents;
    private String name;
    private String password;
}
