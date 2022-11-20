package com.example.studentrating.repositories;

import com.example.studentrating.models.Notification;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.CrudRepository;

import java.util.ArrayList;

public interface NotificationRepository extends CrudRepository<Notification, Long> {

    ArrayList<Notification> findAllByStudent_Id(Sort createdAt, Long studentId);
}
