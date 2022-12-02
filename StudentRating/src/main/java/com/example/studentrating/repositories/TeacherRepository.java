package com.example.studentrating.repositories;

import com.example.studentrating.models.Teacher;
import org.springframework.data.repository.CrudRepository;

import java.util.ArrayList;

public interface TeacherRepository extends CrudRepository<Teacher, Long> {

    ArrayList<Teacher> findAll();

    Teacher findByLogin(String login);

    Teacher findByLoginAndPassword(String login, String password);

}
