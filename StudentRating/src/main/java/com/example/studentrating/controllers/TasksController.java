package com.example.studentrating.controllers;

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
        if (isAuthorize(session)) {
            ArrayList<Task> tasks = taskRepository.findAllByStudentCountIsNot(Sort.by(Sort.Direction.ASC, "deadLine"), 0);
            ArrayList<Respond> responds = respondRepository.findAllByExecutor_IdAndStatus(getSessionId(session), "BUSY");
            ArrayList<Long> respondsIds = new ArrayList<>();
            for (Respond r : responds) {
                if (r.getExecutor().getId() == getSessionId(session)) respondsIds.add(r.getTask().getId());
            }
            ArrayList<Notification> notifications = notificationRepository.findAllByStudent_Id(Sort.by(Sort.Direction.DESC, "createdAt"), getSessionId(session));
            model.addAttribute("notifications", notifications);
            model.addAttribute("tasks", tasks);
            model.addAttribute("respondsIds", respondsIds);
            model.addAttribute("title", "Поручения");
            return "tasks";
        } else return "redirect:/pages/signIn";
    }

    @GetMapping("/getByUser")
    public String getByUser(Model model, HttpSession session) {
        if (isAuthorize(session)) {
            ArrayList<Respond> responds = respondRepository.findAllByExecutor_IdAndStatus(getSessionId(session), "BUSY");
            ArrayList<Notification> notifications = notificationRepository.findAllByStudent_Id(Sort.by(Sort.Direction.DESC, "createdAt"), getSessionId(session));
            model.addAttribute("notifications", notifications);
            model.addAttribute("responds", responds);
            model.addAttribute("title", "Мои поручения");
            return "myTasks";
        } else return "redirect:/pages/signIn";
    }

    public boolean isAuthorize(HttpSession request) {
        if (request.getAttribute("id") != null && request.getAttribute("type") != null) return true;
        else return false;
    }

    public Long getSessionId(HttpSession request) {
        return (Long) request.getAttribute("id");
    }
}
