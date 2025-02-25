package com.example.demo.Servis;

import com.example.demo.IServis.IReview;
import com.example.demo.model.Review;
import com.example.demo.model.User;
import com.example.demo.repo.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;

@Service
public class ReviewService implements IReview {
    @Autowired
    private ReviewRepository reviewRepository;
    public ResponseEntity<?> PostReview(Review review) {
        try {
            if (review == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("Отзыв не может быть пустым");
            }
            reviewRepository.save(review);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body("Отзыв успешно сохранён");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Ошибка при сохранении отзыва: " + e.getMessage());
        }
    }
    public ResponseEntity<?> getAll(int taskId) {
        try {
            Iterable<Review> reviewIterable = reviewRepository.findAll();
            List<Review> reviews = new ArrayList<>();

            if (reviewIterable != null) {
                reviewIterable.forEach(reviews::add);
                reviews.removeIf(review -> review.getTaskId() != taskId);

                if (reviews.isEmpty()) {
                    return ResponseEntity.status(HttpStatus.NO_CONTENT)
                            .body("Нет отзывов для этой задачи");
                }
                return ResponseEntity.ok(reviews);
            } else {
                return ResponseEntity.status(HttpStatus.NO_CONTENT)
                        .body("Список отзывов пуст");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Ошибка при получении отзывов: " + e.getMessage());
        }
    }

}
