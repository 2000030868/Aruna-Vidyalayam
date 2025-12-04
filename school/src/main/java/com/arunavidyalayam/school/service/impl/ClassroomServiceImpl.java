package com.arunavidyalayam.school.service.impl;

import com.arunavidyalayam.school.model.Classroom;
import com.arunavidyalayam.school.model.Student;
import com.arunavidyalayam.school.repository.ClassroomRepository;
import com.arunavidyalayam.school.repository.StudentRepository;
import com.arunavidyalayam.school.service.ClassroomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ClassroomServiceImpl implements ClassroomService {

    @Autowired
    private ClassroomRepository classroomRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Override
    public Classroom saveClassroom(Classroom classroom) {
        return classroomRepository.save(classroom);
    }

    @Override
    public List<Classroom> getAllClassrooms() {
        return classroomRepository.findAll();
    }

    @Override
    public Classroom getClassroomById(Long id) {
        return classroomRepository.findById(id).orElse(null);
    }

    @Override
    public Classroom updateClassroom(Long id, Classroom updatedClassroom) {
        Classroom existing = classroomRepository.findById(id).orElse(null);
        if (existing != null) {
            existing.setName(updatedClassroom.getName());
            existing.setSection(updatedClassroom.getSection());
            existing.setTeacher(updatedClassroom.getTeacher());
            return classroomRepository.save(existing);
        }
        return null;
    }

    @Override
    public void deleteClassroom(Long id) {
        classroomRepository.deleteById(id);
    }

    @Override
    public List<Student> getStudentsByClassroomId(Long classroomId)
    {
       return studentRepository.findByClassroomId(classroomId);
    }
}
