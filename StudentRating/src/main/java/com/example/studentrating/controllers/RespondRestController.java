package com.example.studentrating.controllers;

import com.example.studentrating.lib.Session;
import com.example.studentrating.models.*;
import com.example.studentrating.repositories.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Random;

@RestController
@RequestMapping(path = "/respondsRest")
public class RespondRestController {

    private static final Logger log = LoggerFactory.getLogger(RespondRestController.class);

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private RespondRepository respondRepository;

    @Autowired
    private DisputeRepository disputeRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private TeacherRepository teacherRepository;

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

                        Dispute dispute = new Dispute();

                        dispute.setSum(0);
                        dispute.setRespond(respond);

                        Teacher teacher = teacherRepository.findRandom();
                        dispute.setTeacher(teacher);

                        Student student = studentRepository.findRandomCouncil(respond.getExecutor().getId());
                        dispute.setStudent(student);

                        disputeRepository.save(dispute);
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
