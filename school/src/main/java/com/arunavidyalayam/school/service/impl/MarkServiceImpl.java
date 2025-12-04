package com.arunavidyalayam.school.service.impl;

import com.arunavidyalayam.school.model.Classroom;
import com.arunavidyalayam.school.model.Mark;
import com.arunavidyalayam.school.model.Student;
import com.arunavidyalayam.school.repository.ClassroomRepository;
import com.arunavidyalayam.school.repository.MarkRepository;
import com.arunavidyalayam.school.repository.StudentRepository;
import com.arunavidyalayam.school.service.MarkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MarkServiceImpl implements MarkService {

    @Autowired
    private MarkRepository markRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private ClassroomRepository classroomRepository;

    @Override
    public Mark save(Mark mark) {
        return markRepository.save(mark);
    }

    @Override
    public List<Mark> findByStudent(Long studentId) {
        Student s = studentRepository.findById(studentId).orElseThrow(() -> new RuntimeException("Student not found"));
        return markRepository.findByStudent(s);
    }

    @Override
    public List<Mark> findByClassroom(Long classroomId) {
        Classroom c = classroomRepository.findById(classroomId).orElseThrow(() -> new RuntimeException("Classroom not found"));
        return markRepository.findByClassroom(c);
    }

    @Override
    public List<Mark> findByClassroomAndExamType(Long classroomId, String examType) {
        Classroom c = classroomRepository.findById(classroomId)
                .orElseThrow(() -> new RuntimeException("Classroom not found"));
        return markRepository.findByClassroomAndExamType(c, examType);
    }

    @Override
    public List<Mark> findByStudentAndExamType(Long studentId, String examType) {
        Student s = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));
        return markRepository.findByStudentAndExamType(s, examType);
    }

    @Override
    public List<Mark> findByClassroomAndSubjectAndExamType(Long classroomId, String subject, String examType) {
        Classroom c = classroomRepository.findById(classroomId)
                .orElseThrow(() -> new RuntimeException("Classroom not found"));

        // prefer case-insensitive query if you added it
        if (subject != null && examType != null) {
            // use ignore-case variant if available
            try {
                return markRepository.findByClassroomAndSubjectIgnoreCaseAndExamTypeIgnoreCase(c, subject, examType);
            } catch (NoSuchMethodError | AbstractMethodError ignored) {
                // fallback to case-sensitive variant
                return markRepository.findByClassroomAndSubjectAndExamType(c, subject, examType);
            }
        }
        // fallback: if subject or examType null delegate to other methods (controller will prefer this method only when both provided)
        throw new IllegalArgumentException("subject and examType are required for this query");
    }

    @Override
    public List<Mark> findByStudentAndSubjectAndExamType(Long studentId, String subject, String examType) {
        Student s = studentRepository.findById(studentId).orElseThrow(() -> new RuntimeException("Student not found"));
        try {
            return markRepository.findByStudentAndSubjectIgnoreCaseAndExamTypeIgnoreCase(s, subject, examType);
        } catch (AbstractMethodError | NoSuchMethodError ignored) {
            return markRepository.findByStudentAndSubjectAndExamType(s, subject, examType);
        }
    }


}
