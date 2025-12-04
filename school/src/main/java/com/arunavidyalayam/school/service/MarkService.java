package com.arunavidyalayam.school.service;

import com.arunavidyalayam.school.model.Mark;

import java.util.List;

public interface MarkService {
    Mark save(Mark mark);
    List<Mark> findByStudent(Long studentId);
    List<Mark> findByClassroom(Long classroomId);
    List<Mark> findByClassroomAndExamType(Long classroomId, String examType);
    List<Mark> findByStudentAndExamType(Long studentId, String examType);
    List<Mark> findByClassroomAndSubjectAndExamType(Long classroomId, String subject, String examType);
    List<Mark> findByStudentAndSubjectAndExamType(Long studentId, String subject, String examType);
}
