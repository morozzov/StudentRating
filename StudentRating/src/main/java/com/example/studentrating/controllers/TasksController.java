package com.example.studentrating.controllers;

import com.example.studentrating.models.Task;
import com.example.studentrating.repositories.TasksRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;

@Controller
@RequestMapping("/tasks")
public class TasksController {

    @Autowired
    private TasksRepository tasksRepository;

    @GetMapping("/getAll")
    public String getAll(Model model) {
        ArrayList<Task> tasks = tasksRepository.findAll(Sort.by(Sort.Direction.ASC,"deadLine"));
        model.addAttribute("tasks", tasks);
        model.addAttribute("title", "Поручения");
        return "tasks";
    }

    @GetMapping("/getByUser")
    public String getByUser(Model model) {
        model.addAttribute("title", "Мои поручения");
        return "myTasks";
    }
}
