package com.arunavidyalayam.school.service;

import com.arunavidyalayam.school.model.Classroom;
import com.arunavidyalayam.school.model.Student;

import java.util.List;

public interface ClassroomService {
    Classroom saveClassroom(Classroom classroom);
    List<Classroom> getAllClassrooms();
    Classroom getClassroomById(Long id);
    Classroom updateClassroom(Long id, Classroom updatedClassroom);
    void deleteClassroom(Long id);
    List<Student> getStudentsByClassroomId(Long classroomId);
}
