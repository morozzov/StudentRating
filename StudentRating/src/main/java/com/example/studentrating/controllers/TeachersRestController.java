package com.example.studentrating.controllers;

import com.example.studentrating.models.Student;
import com.example.studentrating.repositories.StudentRepository;
import com.example.studentrating.repositories.TeacherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

import static com.example.studentrating.lib.Encrypt.encryptText;

@RestController
@RequestMapping(path = "/teachersRest")
public class TeachersRestController {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private TeacherRepository teacherRepository;
    
    @PostMapping("/addNew")
    public String addNew(String surname, String name, String patronymic, String login, String group, @RequestParam(defaultValue = "false") boolean isStudentCouncil, HttpSession session) {
        try {
            if (studentRepository.findByLogin(login) == null && teacherRepository.findByLogin(login) == null) {
                Student student = new Student();
                student.setSurname(surname);
                student.setName(name);
                student.setPatronymic(patronymic);
                student.setLogin(login);
                student.setGroupName(group);
                student.setStudentCouncil(isStudentCouncil);
                student.setPassword(encryptText("123"));
                student.setImageUrl("/img/avatars/personal.svg");
                student.setRole("STUDENT");
                student.setPoints(0);
                studentRepository.save(student);
                return "success";
            } else {
                return "Введите другой логин";
            }

//            return student;
        } catch (Exception e) {
            return e.getMessage();
        }
    }
}
