package com.arunavidyalayam.school.repository;

import com.arunavidyalayam.school.model.Mark;
import com.arunavidyalayam.school.model.Classroom;
import com.arunavidyalayam.school.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MarkRepository extends JpaRepository<Mark, Long> {
    List<Mark> findByStudent(Student student);
    List<Mark> findByClassroom(Classroom classroom);
    List<Mark> findByClassroomAndExamType(Classroom classroom, String examType);
    List<Mark> findByStudentAndExamType(Student student, String examType);
    List<Mark> findByClassroomAndSubjectAndExamType(Classroom classroom, String subject, String examType);
    List<Mark> findByClassroomAndSubjectIgnoreCaseAndExamTypeIgnoreCase(Classroom classroom, String subject, String examType);
    List<Mark> findByStudentAndSubjectAndExamType(Student student, String subject, String examType);
    List<Mark> findByStudentAndSubjectIgnoreCaseAndExamTypeIgnoreCase(Student student, String subject, String examType);
}
