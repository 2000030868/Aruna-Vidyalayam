package com.arunavidyalayam.school.service;

import com.arunavidyalayam.school.model.Student;
import java.util.List;

public interface StudentService {
    Student addStudent(Student student);
    List<Student> getAllStudents();
    Student getStudentById(Long id);
    Student updateStudent(Long id, Student student);
    void deleteStudent(Long id);
   Student assignToClassroom(Long studentId, Long classroomId);
    Student saveStudent(Student student);
}
