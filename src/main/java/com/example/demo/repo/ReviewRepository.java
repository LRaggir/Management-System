package com.example.demo.repo;

import com.example.demo.model.Review;
import com.example.demo.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;



@Repository
public interface ReviewRepository extends CrudRepository<Review,Integer> {
    Page<Review> findByTaskId(int task_id, Pageable pageable);
}
