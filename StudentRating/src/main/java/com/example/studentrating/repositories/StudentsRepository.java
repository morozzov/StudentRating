package com.example.studentrating.repositories;

import com.example.studentrating.models.Student;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.CrudRepository;

import java.util.ArrayList;

public interface StudentsRepository extends CrudRepository<Student, Long> {

    ArrayList<Student> findAll(Sort points);
}
