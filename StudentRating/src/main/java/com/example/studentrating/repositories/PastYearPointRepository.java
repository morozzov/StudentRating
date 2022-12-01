package com.example.studentrating.repositories;

import com.example.studentrating.models.PastYearPoint;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.CrudRepository;

import java.util.ArrayList;

public interface PastYearPointRepository extends CrudRepository<PastYearPoint, Long> {

    ArrayList<PastYearPoint> findAllByStudent_Id(Sort year, Long studentId);
    ArrayList<PastYearPoint> findAllByStudent_Id( Long studentId);
}
