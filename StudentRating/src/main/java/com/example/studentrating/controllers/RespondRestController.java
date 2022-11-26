package com.example.studentrating.controllers;

import com.example.studentrating.models.Notification;
import com.example.studentrating.models.Respond;
import com.example.studentrating.models.Student;
import com.example.studentrating.models.Task;
import com.example.studentrating.repositories.NotificationRepository;
import com.example.studentrating.repositories.RespondRepository;
import com.example.studentrating.repositories.StudentRepository;
import com.example.studentrating.repositories.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;

@RestController
@RequestMapping(path = "/respondsRest")
public class RespondRestController {

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
            if (respondRepository.findByTask_IdAndExecutor_IdAndStatus(taskId, getSessionId(session), "BUSY").size() == 0) {
                Task task = taskRepository.findById(taskId).get();
                if (task.getStudentCount() > 0) {
                    Respond respond = new Respond();
                    respond.setStatus("BUSY");
                    respond.setExecutor(studentRepository.findById(getSessionId(session)).get());
                    respond.setTask(task);
                    respondRepository.save(respond);
                    task.setStudentCount(task.getStudentCount() - 1);
                    taskRepository.save(task);
                    return "success";
                } else {
                    return "Закончились свободные места для выполнения поручения";
                }
            } else {
                return "Вы уже откликнулись на данное поручение";
            }

        } catch (Exception e) {
            return "e.getMessage()";
        }
    }

    @PostMapping("/cancelRespondByUserAndRespond")
    public String cancelRespondByUserAndRespond(Long respondId, HttpSession session) {
        try {
            Respond respond = respondRepository.findById(respondId).get();
            if (respond.getExecutor().getId().equals(getSessionId(session))) {
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
                    return "success";
                } else {
                    return "Не существует выбранного активного отклика";
                }
            } else {
                return "Вы не откликались на данное поручение";
            }

        } catch (Exception e) {
            return e.getMessage();
        }
    }

    @PostMapping("/disputeRespond")
    public String disputeRespond(Long respondId, HttpSession session) {
        try {
            Respond respond = respondRepository.findById(respondId).get();
            if (respond.getExecutor().getId().equals(getSessionId(session))) {
                if (!respond.isDisputed()) {
                    respond.setDisputed(true);
                    respondRepository.save(respond);
                    Notification notification = new Notification();
                    notification.setCost(0);
                    notification.setTitle(respond.getTask().getTitle() + " - ОСПАРИВАЕТСЯ");
                    notification.setStudent(respond.getExecutor());
                    notificationRepository.save(notification);

                    //TODO: realize logic for dispute to mentors sending

                    return "success";
                } else {
                    return "Спор уже создан";
                }
            } else {
                return "Вы не откликались на данное поручение";
            }
        } catch (Exception e) {
            return "e.getMessage()";
        }
    }

    public Long getSessionId(HttpSession request) {
        return (Long) request.getAttribute("id");
    }
}
