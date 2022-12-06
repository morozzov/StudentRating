package com.example.studentrating.repositories;

import com.example.studentrating.models.Student;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.CrudRepository;

import java.util.ArrayList;

public interface StudentRepository extends CrudRepository<Student, Long> {

    ArrayList<Student> findAll(Sort sort);

    Page<Student> findAll(Pageable pageable);

    Student findByLogin(String login);

    Student findByLoginAndPassword(String login, String password);
}
