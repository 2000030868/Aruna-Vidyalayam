package com.arunavidyalayam.school.controller;

import com.arunavidyalayam.school.model.Classroom;
import com.arunavidyalayam.school.model.Student;
import com.arunavidyalayam.school.service.ClassroomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/classrooms")
public class ClassroomController
{
    @Autowired
    private ClassroomService classroomService;

    @PostMapping
    public Classroom createClassroom(@RequestBody Classroom classroom)
    {
        return classroomService.saveClassroom(classroom);
    }

    @GetMapping()
    public List<Classroom> getAllClassrooms()
    {
        return classroomService.getAllClassrooms();
    }

    @GetMapping("/{id}")
    public Classroom getClassroomById(@PathVariable Long id)
    {
        return classroomService.getClassroomById(id);
    }

    @PutMapping("/{id}")
    public Classroom updateClassroom(@PathVariable Long id, @RequestBody Classroom classroom)
    {
        return classroomService.updateClassroom(id, classroom);
    }

    @DeleteMapping("/{id}")
    public void deleteClassroom(@PathVariable Long id)
    {
        classroomService.deleteClassroom(id);
    }

    @GetMapping("/{id}/students")
    public List<Student>  getStudentsByClassroom(@PathVariable Long id)
    {
        return classroomService.getStudentsByClassroomId(id);
    }
}
