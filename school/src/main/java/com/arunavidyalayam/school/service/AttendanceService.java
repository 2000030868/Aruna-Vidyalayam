package com.arunavidyalayam.school.service;

import com.arunavidyalayam.school.dto.AttendanceDto;
import com.arunavidyalayam.school.model.Attendance;
import java.time.LocalDate;
import java.util.List;

public interface AttendanceService {
    Attendance saveFromDto(AttendanceDto dto);
    List<Attendance> findByClassAndDate(Long classroomId, LocalDate date);
    List<Attendance> findByStudent(Long studentId);
    List<Attendance> saveBulk(List<AttendanceDto> dtos);
    List<Attendance> findByClassAndDateRange(Long classroomId, LocalDate from, LocalDate to);
}
