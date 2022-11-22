package com.example.studentrating.controllers;

import com.example.studentrating.dto.StudentDTO;
import com.example.studentrating.models.Student;
import com.example.studentrating.repositories.NotificationRepository;
import com.example.studentrating.repositories.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.io.File;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

@RestController
@RequestMapping(path = "/api")
public class ApiController {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private NotificationRepository notificationRepository;
//
//    @GetMapping("/getById/{id}")
//    public StudentDTO getById(@PathVariable("id") Long id) {
//        Student student = studentRepository.findById(id).get();
//
//        StudentDTO studentDTO = new StudentDTO(student);
//
//        return studentDTO;
//    }

    @GetMapping("/getAll")
    public ArrayList<StudentDTO> getAll() {
        ArrayList<Student> students = studentRepository.findAll(Sort.by(Sort.Direction.DESC, "points"));

        ArrayList<StudentDTO> studentDtoList = new ArrayList<>();

        for (var entityItem : students) {
            studentDtoList.add(new StudentDTO(entityItem));
        }

        return studentDtoList;
    }

    @GetMapping("/getByLogin/{login}")
    public StudentDTO getByLogin(@PathVariable("login") String login) {
        Student student = studentRepository.findByLogin(login);

        StudentDTO studentDTO = new StudentDTO(student);

        return studentDTO;
    }
}
