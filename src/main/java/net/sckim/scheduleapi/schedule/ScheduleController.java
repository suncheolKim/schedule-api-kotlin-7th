package net.sckim.scheduleapi.schedule;

import net.sckim.scheduleapi.schedule.dto.DeleteScheduleRequest;
import net.sckim.scheduleapi.schedule.dto.EditScheduleRequest;
import net.sckim.scheduleapi.schedule.dto.ScheduleResponse;
import net.sckim.scheduleapi.schedule.dto.CreateScheduleRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
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

@RestController
@RequestMapping("/api")
public class ScheduleController {
    private final ScheduleService scheduleService;

    public ScheduleController(ScheduleServiceImpl scheduleService) {
        this.scheduleService = scheduleService;
    }

    @PostMapping("/schedules")
    public ScheduleResponse schedule(@RequestBody CreateScheduleRequest createRequest) {
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

    @PutMapping("/schedules/{scheduleId}")
    public ResponseEntity<ScheduleResponse> editSchedule(@PathVariable Long scheduleId, @RequestBody EditScheduleRequest editRequest) {
        final ScheduleResponse editedSchedule = scheduleService.editSchedule(scheduleId, editRequest);
        return new ResponseEntity<>(editedSchedule, HttpStatus.OK);
    }

    @DeleteMapping("/schedules/{scheduleId}")
    public ResponseEntity<Void> deleteSchedule(@PathVariable Long scheduleId, @RequestBody DeleteScheduleRequest deleteRequest) {
        scheduleService.deleteSchedule(scheduleId, deleteRequest);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
