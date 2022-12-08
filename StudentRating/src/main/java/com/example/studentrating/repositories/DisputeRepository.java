package com.example.studentrating.repositories;

import com.example.studentrating.models.Dispute;
import com.example.studentrating.models.Teacher;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.CrudRepository;

import java.util.ArrayList;

public interface DisputeRepository extends CrudRepository<Dispute, Long> {

    ArrayList<Dispute> findAllByStudent_Id(Long studentId);

    ArrayList<Dispute> findAllByTeacher_Id(Long teacherId);
}
