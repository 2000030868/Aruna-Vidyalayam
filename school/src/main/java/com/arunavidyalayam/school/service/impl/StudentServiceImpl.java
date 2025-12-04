package com.arunavidyalayam.school.service.impl;

import com.arunavidyalayam.school.model.Classroom;
import com.arunavidyalayam.school.model.Student;
import com.arunavidyalayam.school.repository.ClassroomRepository;
import com.arunavidyalayam.school.repository.StudentRepository;
import com.arunavidyalayam.school.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudentServiceImpl implements StudentService {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private ClassroomRepository classroomRepository;

    /**
     * Adds a new student, with classroom mapping if provided
     */
//    @Override
//    public Student addStudent(Student student) {
//        if (student.getClassroom() != null && student.getClassroom().getId() != null) {
//            Classroom classroom = classroomRepository.findById(student.getClassroom().getId())
//                    .orElseThrow(() -> new RuntimeException("Classroom not found"));
//            student.setClassroom(classroom);
//
//            classroom.setStrength(classroom.getStrength()+1);
//            classroomRepository.save(classroom);
//        }
//        return studentRepository.save(student);
//    }

    @Override
    public Student addStudent(Student student) {
        if (student.getClassroom() != null && student.getClassroom().getId() != null) {
            Classroom classroom = classroomRepository.findById(student.getClassroom().getId())
                    .orElseThrow(() -> new RuntimeException("Classroom not found"));

            // just link the student to the classroom
            student.setClassroom(classroom);
        }

        return studentRepository.save(student);
    }




//    @Override
//    public Student addStudent(Student student) {
//        if (student.getClassroom() != null && student.getClassroom().getId() != null) {
//            Classroom classroom = classroomRepository.findById(student.getClassroom().getId())
//                    .orElseThrow(() -> new RuntimeException("Classroom not found"));
//
//            student.setClassroom(classroom);
//
//            // ✅ Add student to classroom list
//            classroom.getStudents().add(student);
//            classroomRepository.save(classroom);
//        }
//
//        return studentRepository.save(student);
//    }




    @Override
    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    @Override
    public Student getStudentById(Long id) {
        return studentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Student not found"));
    }

    /**
     * Updates student details and updates classroom mapping if provided
     */


//    @Override
//    public Student updateStudent(Long id, Student studentDetails) {
//        Student existing = studentRepository.findById(id)
//                .orElseThrow(() -> new RuntimeException("Student not found"));
//
//        // Store the old classroom reference before changing
//        Classroom oldClassroom = existing.getClassroom();
//
//        // Update simple fields
//        existing.setName(studentDetails.getName());
//        existing.setRollNumber(studentDetails.getRollNumber());
//        existing.setStandard(studentDetails.getStandard());
//        existing.setEmail(studentDetails.getEmail());
//        existing.setAge(studentDetails.getAge());
//        existing.setGender(studentDetails.getGender());
//
////        // ✅ Update classroom if changed
////        if (studentDetails.getClassroom() != null && studentDetails.getClassroom().getId() != null) {
////            Classroom newClassroom = classroomRepository.findById(studentDetails.getClassroom().getId())
////                    .orElseThrow(() -> new RuntimeException("Classroom not found"));
////
////            // If student is changing classrooms
////            if (oldClassroom != null && !oldClassroom.getId().equals(newClassroom.getId())) {
////                // Decrease old classroom strength
////                oldClassroom.setStrength(Math.max(0, oldClassroom.getStrength() - 1));
////                classroomRepository.save(oldClassroom);
////
////                // Increase new classroom strength
////                newClassroom.setStrength(newClassroom.getStrength() + 1);
////                classroomRepository.save(newClassroom);
////            }
////
////            existing.setClassroom(newClassroom);
////        }
//
//        if (studentDetails.getClassroom() != null && studentDetails.getClassroom().getId() != null) {
//            Classroom newClassroom = classroomRepository.findById(studentDetails.getClassroom().getId())
//                    .orElseThrow(() -> new RuntimeException("Classroom not found"));
//
//            // If student did not have classroom earlier
//            if (oldClassroom == null) {
//                newClassroom.setStrength(newClassroom.getStrength() + 1);
//                classroomRepository.save(newClassroom);
//            }
//            // If classroom changed
//            else if (!oldClassroom.getId().equals(newClassroom.getId())) {
//                oldClassroom.setStrength(Math.max(0, oldClassroom.getStrength() - 1));
//                classroomRepository.save(oldClassroom);
//
//                newClassroom.setStrength(newClassroom.getStrength() + 1);
//                classroomRepository.save(newClassroom);
//            }
//
//            existing.setClassroom(newClassroom);
//        }
//
//
//        return studentRepository.save(existing);
//    }

//    @Override
//    public Student updateStudent(Long id, Student studentDetails) {
//        Student existing = studentRepository.findById(id)
//                .orElseThrow(() -> new RuntimeException("Student not found"));
//
//        Classroom oldClassroom = existing.getClassroom();
//
//        existing.setName(studentDetails.getName());
//        existing.setRollNumber(studentDetails.getRollNumber());
//        existing.setEmail(studentDetails.getEmail());
//        existing.setAge(studentDetails.getAge());
//        existing.setGender(studentDetails.getGender());
//
//        if (studentDetails.getClassroom() != null && studentDetails.getClassroom().getId() != null) {
//            Classroom newClassroom = classroomRepository.findById(studentDetails.getClassroom().getId())
//                    .orElseThrow(() -> new RuntimeException("Classroom not found"));
//
//            // ✅ Remove student from old classroom
//            if (oldClassroom != null && !oldClassroom.getId().equals(newClassroom.getId())) {
//                oldClassroom.getStudents().remove(existing);
//                classroomRepository.save(oldClassroom);
//
//                newClassroom.getStudents().add(existing);
//                classroomRepository.save(newClassroom);
//            }
//
//            existing.setClassroom(newClassroom);
//        }
//
//        return studentRepository.save(existing);
//    }


    @Override
    public Student updateStudent(Long id, Student studentDetails) {
        Student existing = studentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Student not found"));

        // Store the old classroom reference before changing
        Classroom oldClassroom = existing.getClassroom();

        // Update simple fields
        existing.setName(studentDetails.getName());
        existing.setRollNumber(studentDetails.getRollNumber());
        existing.setStandard(studentDetails.getStandard());
        existing.setEmail(studentDetails.getEmail());
        existing.setAge(studentDetails.getAge());
        existing.setGender(studentDetails.getGender());

        // Classroom update logic (already fine)
        if (studentDetails.getClassroom() != null && studentDetails.getClassroom().getId() != null) {
            Classroom newClassroom = classroomRepository.findById(studentDetails.getClassroom().getId())
                    .orElseThrow(() -> new RuntimeException("Classroom not found"));
            existing.setClassroom(newClassroom);
        }

        return studentRepository.save(existing);
    }




    @Override
    public void deleteStudent(Long id) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Student not found"));

        Classroom classroom = student.getClassroom();
        if (classroom != null) {
            // Remove the student from classroom’s student list
            classroom.getStudents().remove(student);

            // Save classroom to update its strength (student count)
            classroomRepository.save(classroom);
        }

        // Now delete the student
        studentRepository.delete(student);
    }


    /**
     * Assign existing student to a classroom
     */
    @Override
    public Student assignToClassroom(Long studentId, Long classroomId) {
        Student student = getStudentById(studentId);
        Classroom classroom = classroomRepository.findById(classroomId)
                .orElseThrow(() -> new RuntimeException("Classroom not found"));
        student.setClassroom(classroom);
        Student savedStudent = studentRepository.save(student);

        classroom.setStrength(classroom.getStudents().size());
        classroomRepository.save(classroom);

        return savedStudent;
       // return studentRepository.save(student);
    }

    @Override
    public Student saveStudent(Student student) {
        return studentRepository.save(student);
    }
}
