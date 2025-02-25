package com.example.demo.IServis;

import com.example.demo.model.Review;
import org.springframework.http.ResponseEntity;

public interface IReview {
    ResponseEntity<?> PostReview(Review review);
    ResponseEntity<?> getAll(int task_id);

}
