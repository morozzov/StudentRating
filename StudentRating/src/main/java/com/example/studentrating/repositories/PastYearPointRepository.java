package com.example.studentrating.repositories;

import com.example.studentrating.models.PastYearPoints;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.CrudRepository;

import java.util.ArrayList;

public interface PastYearPointRepository extends CrudRepository<PastYearPoints, Long> {

    ArrayList<PastYearPoints> findAllByStudent_Id(Sort year, Long studentId);
}
