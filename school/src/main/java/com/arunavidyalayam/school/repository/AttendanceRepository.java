package com.arunavidyalayam.school.repository;

import com.arunavidyalayam.school.model.Attendance;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDate;
import java.util.List;

public interface AttendanceRepository extends JpaRepository<Attendance, Long> {
    List<Attendance> findByClassroomIdAndDate(Long classroomId, LocalDate date);
    List<Attendance> findByStudentId(Long studentId);
    List<Attendance> findByClassroomIdAndDateBetween(Long classroomId, LocalDate from, LocalDate to);
}
