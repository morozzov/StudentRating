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
@RequestMapping(path = "/tasksRest")
public class TasksRestController {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private RespondRepository respondRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private NotificationRepository notificationRepository;

}
