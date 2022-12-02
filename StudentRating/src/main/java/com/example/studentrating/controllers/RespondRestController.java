package com.example.studentrating.controllers;

import com.example.studentrating.lib.Session;
import com.example.studentrating.models.Notification;
import com.example.studentrating.models.Respond;
import com.example.studentrating.models.Student;
import com.example.studentrating.models.Task;
import com.example.studentrating.repositories.NotificationRepository;
import com.example.studentrating.repositories.RespondRepository;
import com.example.studentrating.repositories.StudentRepository;
import com.example.studentrating.repositories.TaskRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;

@RestController
@RequestMapping(path = "/respondsRest")
public class RespondRestController {

    private static final Logger log = LoggerFactory.getLogger(RespondRestController.class);

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private RespondRepository respondRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private NotificationRepository notificationRepository;

    @PostMapping("/makeRespondByUserAndTask")
    public String makeRespondByUserAndTask(Long taskId, HttpSession session) {
        try {
            if (Session.isAuthorize(session).equals("STUDENT") || Session.isAuthorize(session).equals("ADMIN")) {
                if (respondRepository.findByTask_IdAndExecutor_IdAndStatus(taskId, Session.getSessionId(session), "BUSY").size() == 0) {
                    Task task = taskRepository.findById(taskId).get();
                    if (task.getStudentCount() > 0) {
                        Respond respond = new Respond();
                        respond.setStatus("BUSY");
                        respond.setExecutor(studentRepository.findById(Session.getSessionId(session)).get());
                        respond.setTask(task);
                        respondRepository.save(respond);
                        task.setStudentCount(task.getStudentCount() - 1);
                        taskRepository.save(task);
                        log.info("Respond with id:{} was created", respond.getId());
                        return "success";
                    } else {
                        return "Закончились свободные места для выполнения поручения";
                    }
                } else {
                    return "Вы уже откликнулись на данное поручение";
                }
            } else {
                return "У вас нет прав на данное действие";
            }

        } catch (Exception e) {
            return "e.getMessage()";
        }
    }

    @PostMapping("/cancelRespondByUserAndRespond")
    public String cancelRespondByUserAndRespond(Long respondId, HttpSession session) {
        try {
            if (Session.isAuthorize(session).equals("STUDENT") || Session.isAuthorize(session).equals("ADMIN")) {
                Respond respond = respondRepository.findById(respondId).get();
                if (respond.getExecutor().getId().equals(Session.getSessionId(session))) {
                    if (respond.getStatus().equals("BUSY")) {
                        respond.setStatus("CANCELED");
                        respond.setCompletedAt(LocalDateTime.now());
                        respondRepository.save(respond);
                        Task task = taskRepository.findById(respond.getTask().getId()).get();
                        task.setStudentCount(task.getStudentCount() + 1);
                        taskRepository.save(task);
                        Student student = studentRepository.findById(respond.getExecutor().getId()).get();
                        student.setPoints(student.getPoints() - 1);
                        studentRepository.save(student);
                        Notification notification = new Notification();
                        notification.setCost(-1);
                        notification.setTitle(task.getTitle() + " - ОТКАЗ");
                        notification.setStudent(student);
                        notificationRepository.save(notification);
                        log.info("Respond with id:{} was canceled", respond.getId());
                        return "success";
                    } else {
                        return "Не существует выбранного активного отклика";
                    }
                } else {
                    return "Вы не откликались на данное поручение";
                }
            } else {
                return "У вас нет прав на данное действие";
            }
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    @PostMapping("/disputeRespond")
    public String disputeRespond(Long respondId, HttpSession session) {
        try {
            if (Session.isAuthorize(session).equals("STUDENT") || Session.isAuthorize(session).equals("ADMIN")) {
                Respond respond = respondRepository.findById(respondId).get();
                if (respond.getExecutor().getId().equals(Session.getSessionId(session))) {
                    if (!respond.isDisputed()) {
                        respond.setDisputed(true);
                        respondRepository.save(respond);
                        Notification notification = new Notification();
                        notification.setCost(0);
                        notification.setTitle(respond.getTask().getTitle() + " - ОСПАРИВАЕТСЯ");
                        notification.setStudent(respond.getExecutor());
                        notificationRepository.save(notification);

                        //TODO: realize logic for dispute to mentors sending

                        log.info("Respond with id:{} was disputed", respond.getId());
                        return "success";
                    } else {
                        return "Спор уже создан";
                    }
                } else {
                    return "Вы не откликались на данное поручение";
                }
            } else {
                return "У вас нет прав на данное действие";
            }
        } catch (Exception e) {
            return "e.getMessage()";
        }
    }
}
