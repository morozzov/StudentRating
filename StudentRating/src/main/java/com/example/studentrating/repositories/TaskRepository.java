package com.example.studentrating.repositories;

import com.example.studentrating.models.Task;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.CrudRepository;

import java.util.ArrayList;

public interface TaskRepository extends CrudRepository<Task, Long> {

    ArrayList<Task> findAllByStudentCountIsNotAndActive(Sort deadLin, int count, boolean active);

    ArrayList<Task> findAllByAuthor_Id(Long id);

    ArrayList<Task> findAllByAuthor_IdAndActive(Long id, boolean active);
}
