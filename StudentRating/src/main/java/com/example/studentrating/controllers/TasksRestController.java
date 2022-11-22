package com.example.studentrating.controllers;

import com.example.studentrating.models.Respond;
import com.example.studentrating.models.Task;
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
@RequestMapping(path = "/tasksRest")
public class TasksRestController {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private RespondRepository respondRepository;

    @Autowired
    private StudentRepository studentRepository;

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

    public Long getSessionId(HttpSession request) {
        return (Long) request.getAttribute("id");
    }
}
