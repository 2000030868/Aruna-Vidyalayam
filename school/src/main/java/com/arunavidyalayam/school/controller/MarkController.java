package com.arunavidyalayam.school.controller;

import com.arunavidyalayam.school.model.Mark;
import com.arunavidyalayam.school.service.MarkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/marks")
@CrossOrigin(origins = "*")
public class MarkController {

    @Autowired
    private MarkService markService;

    @PostMapping
    public Mark save(@RequestBody Mark mark) {
        return markService.save(mark);
    }

    @GetMapping("/student/{studentId}")
    public List<Mark> findByStudent(@PathVariable Long studentId) {
        return markService.findByStudent(studentId);
    }

//    @GetMapping("/class/{classroomId}")
//    public ResponseEntity<List<Mark>> findByClassroom(
//            @PathVariable Long classroomId,
//            @RequestParam(value = "examType", required = false) String examType,
//            @RequestParam(value = "studentId", required = false) Long studentId) {
//
//        // priority: if studentId provided -> fetch by student (optionally filter examType)
//        if (studentId != null) {
//            if (examType != null && !examType.isBlank()) {
//                return ResponseEntity.ok(markService.findByStudentAndExamType(studentId, examType));
//            } else {
//                return ResponseEntity.ok(markService.findByStudent(studentId));
//            }
//        }
//
//        // no student filter -> use classroom (with optional examType)
//        if (examType != null && !examType.isBlank()) {
//            return ResponseEntity.ok(markService.findByClassroomAndExamType(classroomId, examType));
//        } else {
//            return ResponseEntity.ok(markService.findByClassroom(classroomId));
//        }
//    }


    @GetMapping("/class/{classroomId}")
    public ResponseEntity<List<Mark>> findByClassroom(
            @PathVariable Long classroomId,
            @RequestParam(value = "examType", required = false) String examType,
            @RequestParam(value = "studentId", required = false) Long studentId,
            @RequestParam(value = "subject", required = false) String subject) {

        // student-priority branch
        if (studentId != null) {
            if (subject != null && examType != null) {
                return ResponseEntity.ok(markService.findByStudentAndSubjectAndExamType(studentId, subject, examType));
            } else if (examType != null) {
                return ResponseEntity.ok(markService.findByStudentAndExamType(studentId, examType));
            } else {
                return ResponseEntity.ok(markService.findByStudent(studentId));
            }
        }

        // classroom branch
        if (subject != null && examType != null) {
            return ResponseEntity.ok(markService.findByClassroomAndSubjectAndExamType(classroomId, subject, examType));
        } else if (examType != null) {
            return ResponseEntity.ok(markService.findByClassroomAndExamType(classroomId, examType));
        } else {
            return ResponseEntity.ok(markService.findByClassroom(classroomId));
        }
    }





}
