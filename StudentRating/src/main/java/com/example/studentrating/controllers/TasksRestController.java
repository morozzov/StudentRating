package com.example.studentrating.controllers;

import com.example.studentrating.lib.Session;
import com.example.studentrating.models.Notification;
import com.example.studentrating.models.Respond;
import com.example.studentrating.models.Student;
import com.example.studentrating.models.Task;
import com.example.studentrating.repositories.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;

import static com.example.studentrating.lib.Encrypt.encryptText;

@RestController
@RequestMapping(path = "/tasksRest")
public class TasksRestController {

    private static final Logger log = LoggerFactory.getLogger(TasksRestController.class);

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private RespondRepository respondRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private TeacherRepository teacherRepository;

    @Autowired
    private NotificationRepository notificationRepository;

    @PostMapping("/addNew")
    public String addNew(String title, String about, int studentCount, int cost, String deadLine, HttpSession session) {
        try {
            if (Session.isAuthorize(session).equals("TEACHER")) {
                Task task = new Task();

                task.setTitle(title);
                task.setAbout(about);
                task.setStudentCount(studentCount);
                task.setCost(cost);
                task.setDeadLine(LocalDateTime.parse(deadLine));
                task.setAuthor(teacherRepository.findById(Session.getSessionId(session)).get());
                task.setActive(true);

                taskRepository.save(task);
                log.info("Task with id:{} was created", task.getId());

                return "success";
            } else {
                return "У вас нет прав на данное действие";
            }
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    @PostMapping("/complete")
    public String complete(Long taskId, HttpSession session) {
        try {
            if (Session.isAuthorize(session).equals("TEACHER")) {
                Task task = taskRepository.findById(taskId).get();

                ArrayList<Respond> responds = respondRepository.findAllByTask_IdAndStatus(taskId, "BUSY");

                if (!responds.isEmpty()) {
                    for (Respond respond : responds) {
                        respond.setCompletedAt(LocalDateTime.now());
                        respond.setStatus("COMPLETED");
                        respondRepository.save(respond);

                        Student student = studentRepository.findById(respond.getExecutor().getId()).get();

                        student.setPoints(student.getPoints() + respond.getTask().getCost());
                        studentRepository.save(student);

                        Notification notification = new Notification();

                        notification.setCost(respond.getTask().getCost());
                        notification.setTitle(respond.getTask().getTitle());
                        notification.setStudent(respond.getExecutor());
                        notificationRepository.save(notification);
                    }
                }

                task.setActive(false);
                taskRepository.save(task);
                log.info("Task with id:{} was completed", task.getId());

                return "success";
            } else {
                return "У вас нет прав на данное действие";
            }
        } catch (Exception e) {
            return e.getMessage();
        }
    }
}
