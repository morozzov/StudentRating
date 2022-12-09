package com.example.studentrating.repositories;

import com.example.studentrating.models.Teacher;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.ArrayList;

public interface TeacherRepository extends CrudRepository<Teacher, Long> {

    ArrayList<Teacher> findAll();

    Teacher findByLogin(String login);

    Teacher findByLoginAndPassword(String login, String password);

    @Query(value = "select * from teacher order by random() limit 1;", nativeQuery = true)
    Teacher findRandom();
}
