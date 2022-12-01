package com.example.studentrating.repositories;

import com.example.studentrating.models.Activity;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.CrudRepository;

import java.util.ArrayList;

public interface ActivityRepository extends CrudRepository<Activity, Long> {

    ArrayList<Activity> findAllByStudent_Id(Sort createdAt, Long studentId);

    ArrayList<Activity> findAllByStudent_Id(Long studentId);
}
