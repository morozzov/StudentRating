package com.example.studentrating.repositories;

import com.example.studentrating.models.Respond;
import org.springframework.data.repository.CrudRepository;

import java.util.ArrayList;

public interface RespondRepository extends CrudRepository<Respond, Long> {

    ArrayList<Respond> findAllByExecutor_IdAndStatus(Long id, String status);

    ArrayList<Respond> findByTask_IdAndExecutor_IdAndStatus(Long taskId, Long executorId, String status);
}
