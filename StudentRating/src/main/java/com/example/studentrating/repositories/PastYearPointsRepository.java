package com.example.studentrating.repositories;

import com.example.studentrating.models.PastYearPoints;
import com.example.studentrating.models.Task;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.CrudRepository;

import java.util.ArrayList;

public interface PastYearPointsRepository extends CrudRepository<PastYearPoints, Long> {

    ArrayList<PastYearPoints> findAllByStudent(Sort year, Long studentId);
}
