package net.sckim.scheduleapi.schedule;

import jakarta.validation.Valid;
import net.sckim.scheduleapi.exception.EntityNotFoundException;
import net.sckim.scheduleapi.exception.PasswordMismatchedException;
import net.sckim.scheduleapi.schedule.dto.DeleteScheduleRequest;
import net.sckim.scheduleapi.schedule.dto.EditScheduleRequest;
import net.sckim.scheduleapi.schedule.dto.ScheduleResponse;
import net.sckim.scheduleapi.schedule.dto.CreateScheduleRequest;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class ScheduleController {
    private final ScheduleService scheduleService;

    public ScheduleController(ScheduleServiceImpl scheduleService) {
        this.scheduleService = scheduleService;
    }

    @PostMapping("/schedules")
    public ScheduleResponse schedule(@Valid @RequestBody CreateScheduleRequest createRequest) {
        return scheduleService.create(createRequest);
    }

    @GetMapping("/schedules/{scheduleId}")
    public ResponseEntity<ScheduleResponse> getSchedule(@PathVariable Long scheduleId) {
        return new ResponseEntity<>(scheduleService.getSchedule(scheduleId), HttpStatus.OK);
    }

    @GetMapping("/schedules")
    public ResponseEntity<List<ScheduleResponse>> getScheduleList(@RequestParam(required = false) LocalDate updatedDate,
                                                                  @RequestParam(required = false) Long userId) {
        return new ResponseEntity<>(scheduleService.getScheduleList(updatedDate, userId), HttpStatus.OK);
    }

    @GetMapping("/schedules/pages")
    public ResponseEntity<Page<ScheduleResponse>> getScheduleList(@RequestParam(required = false) LocalDate updatedDate,
                                                                  @RequestParam(required = false) Long userId,
                                                                  @RequestParam(required = false, defaultValue = "1") Integer page,
                                                                  @RequestParam(required = false, defaultValue = "10") Integer size) {
        return new ResponseEntity<>(scheduleService.getSchedulePage(updatedDate, userId, page, size), HttpStatus.OK);
    }

    @PutMapping("/schedules/{scheduleId}")
    public ResponseEntity<ScheduleResponse> editSchedule(@PathVariable Long scheduleId, @Valid @RequestBody EditScheduleRequest editRequest) {
        final ScheduleResponse editedSchedule = scheduleService.editSchedule(scheduleId, editRequest);
        return new ResponseEntity<>(editedSchedule, HttpStatus.OK);
    }

    @DeleteMapping("/schedules/{scheduleId}")
    public ResponseEntity<Void> deleteSchedule(@PathVariable Long scheduleId, @Valid @RequestBody DeleteScheduleRequest deleteRequest) {
        scheduleService.deleteSchedule(scheduleId, deleteRequest);
        return new ResponseEntity<>(HttpStatus.OK);
    }


    // FIXME : 리팩토링 필요 - 각 예외를 적절한 HttpStatus 코드 반환하도록 변경
    @ExceptionHandler(value = {PasswordMismatchedException.class, EntityNotFoundException.class})
    public ResponseEntity<String> handleException(Exception e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ResponseEntity<String> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        String errorMessage = e.getBindingResult().getFieldErrors()
                .stream()
                .map(fieldError -> "[" + fieldError.getField() + "] " + fieldError.getDefaultMessage() )
                .collect(Collectors.joining("\n"));

        return new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST);
    }
}
