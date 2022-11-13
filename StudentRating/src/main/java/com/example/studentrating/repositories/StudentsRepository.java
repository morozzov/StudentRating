package com.example.studentrating.repositories;

import com.example.studentrating.models.Student;
import org.springframework.data.repository.CrudRepository;

public interface StudentsRepository extends CrudRepository<Student, Long> {
}
