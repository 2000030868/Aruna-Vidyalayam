package com.arunavidyalayam.school.service.impl;

import com.arunavidyalayam.school.dto.AttendanceDto;
import com.arunavidyalayam.school.model.Attendance;
import com.arunavidyalayam.school.model.Student;
import com.arunavidyalayam.school.model.Classroom;
import com.arunavidyalayam.school.repository.AttendanceRepository;
import com.arunavidyalayam.school.repository.StudentRepository;
import com.arunavidyalayam.school.repository.ClassroomRepository;
import com.arunavidyalayam.school.service.AttendanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class AttendanceServiceImpl implements AttendanceService {

    @Autowired
    private AttendanceRepository attendanceRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private ClassroomRepository classroomRepository;

    @Override
    public Attendance saveFromDto(AttendanceDto dto) {
        if (dto.getStudentId() == null || dto.getClassroomId() == null) {
            throw new IllegalArgumentException("studentId and classroomId are required");
        }

        Student student = studentRepository.findById(dto.getStudentId())
                .orElseThrow(() -> new RuntimeException("Student not found"));
        Classroom classroom = classroomRepository.findById(dto.getClassroomId())
                .orElseThrow(() -> new RuntimeException("Classroom not found"));

        // Optional: verify the student actually belongs to that classroom
        if (student.getClassroom() == null || !Objects.equals(student.getClassroom().getId(), classroom.getId())) {
            throw new IllegalArgumentException("Student " + student.getId() + " does not belong to classroom " + classroom.getId());
        }

        Attendance a = new Attendance();
        a.setStudent(student);
        a.setClassroom(classroom);
        a.setDate(dto.getDate() == null ? LocalDate.now() : dto.getDate());
        a.setPresent(dto.getPresent() == null ? Boolean.FALSE : dto.getPresent());
        a.setRemarks(dto.getRemarks());

        return attendanceRepository.save(a);
    }

    @Override
    public List<Attendance> findByClassAndDate(Long classroomId, LocalDate date) {
        return attendanceRepository.findByClassroomIdAndDate(classroomId, date);
    }

    @Override
    public List<Attendance> findByStudent(Long studentId) {
        return attendanceRepository.findByStudentId(studentId);
    }

    // ---- NEW: bulk save implementation (uses Student.getClassroom()) ----
//    @Override
//    @Transactional
//    public List<Attendance> saveBulk(List<AttendanceDto> dtos) {
//        if (dtos == null || dtos.isEmpty()) {
//            throw new IllegalArgumentException("Attendance list is empty");
//        }
//
//        // collect classroom ids from the incoming DTOs (should be one)
//        Set<Long> classroomIds = dtos.stream()
//                .map(AttendanceDto::getClassroomId)
//                .filter(Objects::nonNull)
//                .collect(Collectors.toSet());
//
//        if (classroomIds.isEmpty()) {
//            throw new IllegalArgumentException("classroomId is required for attendance records");
//        }
//        if (classroomIds.size() > 1) {
//            throw new IllegalArgumentException("All attendance records must belong to the same classroom");
//        }
//        Long classroomId = classroomIds.iterator().next();
//
//        // collect distinct student ids
//        Set<Long> studentIds = dtos.stream()
//                .map(AttendanceDto::getStudentId)
//                .filter(Objects::nonNull)
//                .collect(Collectors.toSet());
//
//        if (studentIds.isEmpty()) {
//            throw new IllegalArgumentException("No student ids provided");
//        }
//
//        // fetch students in a single query
//        List<Student> students = studentRepository.findAllById(studentIds);
//        Set<Long> foundIds = students.stream().map(Student::getId).collect(Collectors.toSet());
//
//        // find missing students
//        Set<Long> missing = studentIds.stream()
//                .filter(id -> !foundIds.contains(id))
//                .collect(Collectors.toSet());
//        if (!missing.isEmpty()) {
//            throw new IllegalArgumentException("Missing students with ids: " + missing);
//        }
//
//        // verify each student has a classroom and belongs to the requested classroom
//        List<Long> notInClass = new ArrayList<>();
//        for (Student s : students) {
//            if (s.getClassroom() == null || s.getClassroom().getId() == null) {
//                notInClass.add(s.getId());
//            } else if (!Objects.equals(s.getClassroom().getId(), classroomId)) {
//                notInClass.add(s.getId());
//            }
//        }
//        if (!notInClass.isEmpty()) {
//            throw new IllegalArgumentException("Students not in classroom " + classroomId + ": " + notInClass);
//        }
//
//        // fetch classroom entity (exists because students already point to it, but still verify)
//        Classroom classroom = classroomRepository.findById(classroomId)
//                .orElseThrow(() -> new IllegalArgumentException("Classroom not found: " + classroomId));
//
//        // map students to a map for quick lookup
//        Map<Long, Student> studentMap = students.stream().collect(Collectors.toMap(Student::getId, s -> s));
//
//        // build attendance entities
//        List<Attendance> entities = dtos.stream().map(dto -> {
//            Attendance a = new Attendance();
//            Student stu = studentMap.get(dto.getStudentId());
//            a.setStudent(stu);
//            a.setClassroom(classroom); // use classroom from repository (or stu.getClassroom())
//            a.setDate(dto.getDate() == null ? LocalDate.now() : dto.getDate());
//            a.setPresent(dto.getPresent() == null ? Boolean.FALSE : dto.getPresent());
//            a.setRemarks(dto.getRemarks());
//            return a;
//        }).collect(Collectors.toList());
//
//        // save all in a single transaction
//        return attendanceRepository.saveAll(entities);
//    }

    @Override
    @Transactional
    public List<Attendance> saveBulk(List<AttendanceDto> dtos) {
        if (dtos == null || dtos.isEmpty()) {
            throw new IllegalArgumentException("Attendance list is empty");
        }

        // Ensure single classroom and single date (frontend sends same date for all)
        Set<Long> classroomIds = dtos.stream()
                .map(AttendanceDto::getClassroomId)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
        if (classroomIds.isEmpty()) throw new IllegalArgumentException("classroomId is required");
        if (classroomIds.size() > 1) throw new IllegalArgumentException("All attendance records must belong to the same classroom");
        Long classroomId = classroomIds.iterator().next();

        Set<LocalDate> dates = dtos.stream()
                .map(AttendanceDto::getDate)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
        if (dates.isEmpty()) throw new IllegalArgumentException("date is required");
        if (dates.size() > 1) throw new IllegalArgumentException("All attendance records must be for the same date (bulk per date).");
        LocalDate date = dates.iterator().next();

        // collect distinct student ids
        Set<Long> studentIds = dtos.stream()
                .map(AttendanceDto::getStudentId)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
        if (studentIds.isEmpty()) throw new IllegalArgumentException("No student ids provided");

        // fetch students & validate existence
        List<Student> students = studentRepository.findAllById(studentIds);
        Set<Long> foundIds = students.stream().map(Student::getId).collect(Collectors.toSet());
        Set<Long> missing = studentIds.stream().filter(id -> !foundIds.contains(id)).collect(Collectors.toSet());
        if (!missing.isEmpty()) throw new IllegalArgumentException("Missing students with ids: " + missing);

        // fetch existing attendance rows for that classroom and date
        List<Attendance> existing = attendanceRepository.findByClassroomIdAndDate(classroomId, date);
        // map existing by studentId for quick lookup
        Map<Long, Attendance> existingByStudent = existing.stream()
                .filter(a -> a.getStudent() != null)
                .collect(Collectors.toMap(a -> a.getStudent().getId(), a -> a));

        // fetch classroom entity one time
        Classroom classroom = classroomRepository.findById(classroomId)
                .orElseThrow(() -> new IllegalArgumentException("Classroom not found: " + classroomId));

        // prepare list of entities to save/update
        List<Attendance> toSave = new ArrayList<>(dtos.size());

        // map students for lookup
        Map<Long, Student> studentMap = students.stream().collect(Collectors.toMap(Student::getId, s -> s));

        for (AttendanceDto dto : dtos) {
            Long sid = dto.getStudentId();
            // existing attendance for this student+date -> update it
            if (existingByStudent.containsKey(sid)) {
                Attendance a = existingByStudent.get(sid);
                a.setPresent(dto.getPresent() == null ? Boolean.FALSE : dto.getPresent());
                a.setRemarks(dto.getRemarks());
                // update date/classroom if needed (usually same)
                a.setDate(date);
                a.setClassroom(classroom);
                toSave.add(a);
            } else {
                // create new attendance entity
                Attendance a = new Attendance();
                a.setStudent(studentMap.get(sid));
                a.setClassroom(classroom);
                a.setDate(date);
                a.setPresent(dto.getPresent() == null ? Boolean.FALSE : dto.getPresent());
                a.setRemarks(dto.getRemarks());
                toSave.add(a);
            }
        }

        // save all updates & inserts
        List<Attendance> saved = attendanceRepository.saveAll(toSave);
        return saved;
    }


    @Override
    public List<Attendance> findByClassAndDateRange(Long classroomId, LocalDate from, LocalDate to) {
        return attendanceRepository.findByClassroomIdAndDateBetween(classroomId, from, to);
    }
}
