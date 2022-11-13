package com.example.studentrating.controllers;

import com.example.studentrating.dtos.StudentDTO;
import com.example.studentrating.models.Student;
import com.example.studentrating.repositories.StudentsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;

@RestController
@RequestMapping(path = "/api")
public class ApiController {

    @Autowired
    private StudentsRepository studentsRepository;

    @GetMapping("/getById/{id}")
    public StudentDTO getById(@PathVariable("id") Long id) {
        Student student = studentsRepository.findById(id).get();

        StudentDTO studentDTO = new StudentDTO(student);

        return studentDTO;
    }

    @GetMapping("/getAll")
    public ArrayList<StudentDTO>  getAll() {
        ArrayList<Student> students = studentsRepository.findAll(Sort.by(Sort.Direction.DESC,"points"));

        ArrayList<StudentDTO> studentDtoList = new ArrayList<>();

        for (var entityItem : students) {
            studentDtoList.add(new StudentDTO(entityItem));
        }

        return studentDtoList;
    }

}
