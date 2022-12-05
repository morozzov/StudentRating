package com.example.studentrating.controllers;

import com.example.studentrating.lib.Session;
import com.example.studentrating.models.*;
import com.example.studentrating.repositories.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

import java.util.ArrayList;

import static com.example.studentrating.lib.Encrypt.encryptText;

@RestController
@RequestMapping(path = "/teachersRest")
public class TeachersRestController {

    private static final Logger log = LoggerFactory.getLogger(TeachersRestController.class);

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private TeacherRepository teacherRepository;

    @Autowired
    private ActivityRepository activityRepository;

    @Autowired
    private TaskRepository taskRepository;

    @PostMapping("/addNew")
    public String addNew(String surname, String name, String patronymic, String login, HttpSession session) {
        try {
            if (Session.isAuthorize(session).equals("ADMIN")) {
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
                    log.info("Teacher with id:{} was created", teacher.getId());

                    return "success";
                } else {
                    return "Введите другой логин";
                }
            } else {
                return "У вас нет прав на данное действие";
            }
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    @PostMapping("/deleteById")
    public String deleteById(Long teacherId, HttpSession session) {
        try {
            if (Session.isAuthorize(session).equals("ADMIN")) {
                ArrayList<Activity> activities = activityRepository.findAllByAuthor_Id(teacherId);

                for (Activity activity : activities) {
                    activity.setAuthor(null);
                }
                activityRepository.saveAll(activities);

                ArrayList<Task> tasks = taskRepository.findAllByAuthor_Id(teacherId);

                for (Task task : tasks) {
                    task.setAuthor(null);
                }
                activityRepository.saveAll(activities);

                teacherRepository.deleteById(teacherId);
                log.info("Teacher with id:{} was deleted", teacherId);

                return "success";
            } else {
                return "У вас нет прав на данное действие";
            }
        } catch (
                Exception e) {
            return e.getMessage();
        }
    }
}
