package com.example.studentrating.controllers;

import com.example.studentrating.lib.Session;
import com.example.studentrating.models.Notification;
import com.example.studentrating.models.Respond;
import com.example.studentrating.models.Task;
import com.example.studentrating.repositories.NotificationRepository;
import com.example.studentrating.repositories.RespondRepository;
import com.example.studentrating.repositories.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;

@Controller
@RequestMapping("/tasks")
public class TasksController {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private RespondRepository respondRepository;

    @Autowired
    private NotificationRepository notificationRepository;

    @GetMapping("/getAll")
    public String getAll(Model model, HttpSession session) {
        if (Session.isAuthorize(session).equals("STUDENT") || Session.isAuthorize(session).equals("ADMIN")) {
            ArrayList<Task> tasks = taskRepository.findAllByStudentCountIsNotAndActive(Sort.by(Sort.Direction.ASC, "deadLine"), 0, true);
            ArrayList<Respond> responds = respondRepository.findAllByExecutor_IdAndStatus(Session.getSessionId(session), "BUSY");
            ArrayList<Long> respondsIds = new ArrayList<>();
            for (Respond r : responds) {
                if (r.getExecutor().getId() == Session.getSessionId(session)) respondsIds.add(r.getTask().getId());
            }
            ArrayList<Notification> notifications = notificationRepository.findAllByStudent_Id(Sort.by(Sort.Direction.DESC, "createdAt"), Session.getSessionId(session));
            model.addAttribute("notifications", notifications);
            model.addAttribute("tasks", tasks);
            model.addAttribute("respondsIds", respondsIds);
            model.addAttribute("title", "Поручения");
            return "tasks";
        } else return "redirect:/pages/signIn";
    }

    @GetMapping("/getByUser")
    public String getByUser(Model model, HttpSession session) {
        if (Session.isAuthorize(session).equals("TEACHER")) {
            ArrayList<Task> tasks = taskRepository.findAllByAuthor_IdAndActive(Session.getSessionId(session), true);
            model.addAttribute("tasks", tasks);
            model.addAttribute("title", "Мои поручения");
            return "myTeacherTasks";
        } else return "redirect:/pages/signIn";
    }
}
