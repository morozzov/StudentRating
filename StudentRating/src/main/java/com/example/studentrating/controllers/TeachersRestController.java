package com.example.studentrating.controllers;

import com.example.studentrating.models.Student;
import com.example.studentrating.models.Teacher;
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
    public String addNew(String surname, String name, String patronymic, String login, HttpSession session) {
        try {
            if (studentRepository.findByLogin(login) == null && teacherRepository.findByLogin(login) == null) {
                Teacher teacher = new Teacher();
                teacher.setSurname(surname);
                teacher.setName(name);
                teacher.setPatronymic(patronymic);
                teacher.setLogin(login);
                teacher.setPassword(encryptText("123"));
                teacher.setImageUrl("/img/avatars/personal.svg");
                teacher.setRole("TEACHER");
                teacherRepository.save(teacher);
                return "success";
            } else {
                return "Введите другой логин";
            }
        } catch (Exception e) {
            return e.getMessage();
        }
    }
}
