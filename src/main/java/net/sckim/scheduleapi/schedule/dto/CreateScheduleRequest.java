package net.sckim.scheduleapi.schedule.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class CreateScheduleRequest {
    @Positive(message = "1 이상의 양수를 입력하세요.")
    private Long userId;

    @Size(min = 1, max = 200)
    @NotBlank
    private String contents;

    @NotBlank
    private String password;
}
