package com.example.studentrating.repositories;

import com.example.studentrating.models.Task;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.CrudRepository;

import java.util.ArrayList;

public interface TasksRepository  extends CrudRepository<Task, Long> {

    ArrayList<Task> findAllByStudentCountIsNot(Sort deadLin, int count);
}
