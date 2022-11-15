package com.example.studentrating.controllers;

import com.example.studentrating.models.Task;
import com.example.studentrating.repositories.TasksRepository;
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
    private TasksRepository tasksRepository;

    @GetMapping("/getAll")
    public String getAll(Model model, HttpSession session) {
        if (isAuthorize(session)) {
            ArrayList<Task> tasks = tasksRepository.findAll(Sort.by(Sort.Direction.ASC, "deadLine"));
            model.addAttribute("tasks", tasks);
            model.addAttribute("title", "Поручения");
            return "tasks";
        } else return "redirect:/pages/signIn";
    }

    @GetMapping("/getByUser")
    public String getByUser(Model model, HttpSession session) {
        if (isAuthorize(session)) {
            model.addAttribute("title", "Мои поручения");
            return "myTasks";
        } else return "redirect:/pages/signIn";
    }

    public boolean isAuthorize(HttpSession request) {
        if (request.getAttribute("id") != null && request.getAttribute("type") != null) return true;
        else return false;
    }
}
