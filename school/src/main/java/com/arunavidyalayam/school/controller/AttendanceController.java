package com.arunavidyalayam.school.controller;

import com.arunavidyalayam.school.dto.AttendanceDto;
import com.arunavidyalayam.school.model.Attendance;
import com.arunavidyalayam.school.service.AttendanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/attendances")
@CrossOrigin(origins = "*")
public class AttendanceController {

    @Autowired
    private AttendanceService attendanceService;

    // single record (existing)
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Attendance save(@RequestBody AttendanceDto attendanceDto) {
        return attendanceService.saveFromDto(attendanceDto);
    }

    // ---- NEW: bulk save endpoint ----
    @PostMapping(path = "/bulk", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> saveBulk(@RequestBody List<AttendanceDto> dtos) {
        if (dtos == null || dtos.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("message", "Attendance list is empty"));
        }
        try {
            List<Attendance> saved = attendanceService.saveBulk(dtos);
            return ResponseEntity.ok(saved);
        } catch (IllegalArgumentException iae) {
            return ResponseEntity.badRequest().body(Map.of("message", iae.getMessage()));
        } catch (Exception ex) {
            ex.printStackTrace();
            return ResponseEntity.status(500).body(Map.of("message", "Server error while saving attendance"));
        }
    }

    @GetMapping("/class/{classroomId}")
    public List<Attendance> byClassAndDate(@PathVariable Long classroomId,
                                           @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return attendanceService.findByClassAndDate(classroomId, date);
    }

    @GetMapping("/student/{studentId}")
    public List<Attendance> byStudent(@PathVariable Long studentId) {
        return attendanceService.findByStudent(studentId);
    }

    @GetMapping("/class/{classroomId}/range")
    public List<Attendance> byClassAndDateRange(
            @PathVariable Long classroomId,
            @RequestParam("from") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam("to")   @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) {
        return attendanceService.findByClassAndDateRange(classroomId, from, to);
    }

}
