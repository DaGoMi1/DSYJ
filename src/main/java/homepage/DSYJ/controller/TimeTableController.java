package homepage.DSYJ.controller;

import homepage.DSYJ.domain.Schedule;
import homepage.DSYJ.service.ScheduleService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/timeTable")
public class TimeTableController {

    private final ScheduleService scheduleService;

    public TimeTableController(ScheduleService scheduleService) {
        this.scheduleService = scheduleService;
    }

    @GetMapping("")
    public String timeTable() {
        return "timetable";
    }

    @PostMapping("/save-schedule")
    public ResponseEntity<String> saveSchedule(@RequestBody Schedule schedule) {
        try {
            // 받은 데이터를 scheduleDTO 객체로 매핑하여 처리
            scheduleService.scheduleSave(schedule);
            // 예시: 받은 데이터를 그대로 응답
            return ResponseEntity.ok("Data saved successfully!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error saving data: " + e.getMessage());
        }
    }
}
