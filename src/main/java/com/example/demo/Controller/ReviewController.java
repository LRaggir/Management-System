package com.example.demo.Controller;

import com.example.demo.IServis.IReview;
import com.example.demo.model.Review;
import com.example.demo.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
public class ReviewController {
    @Autowired
    private IReview iReview;
@CrossOrigin
    @PostMapping("/createRe")
@PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<?> createReview(@Valid @RequestBody Review review, BindingResult bindingResult) {
    if (bindingResult.hasErrors()) {
        List<FieldError> fieldErrors = bindingResult.getFieldErrors();
        StringBuilder errorMessage = new StringBuilder("Ошибка валидации: ");
        for (FieldError error : fieldErrors) {
            errorMessage.append(error.getField())
                    .append(" - ")
                    .append(error.getDefaultMessage())
                    .append("; ");
        }
        return ResponseEntity.badRequest().body(errorMessage.toString());
    } else {
        try {
            return ResponseEntity.ok(iReview.PostReview(review));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Ошибка создания комментария: " + e.getMessage());
        }
    }
}
@CrossOrigin
    @GetMapping("/getRe")
@PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<?> getReview(Integer task_id) {
        try {
            return ResponseEntity.ok( iReview.getAll(task_id));
        } catch (Exception e) {
           return ResponseEntity.badRequest().body("Ошибка получения комментариев: " + e.getMessage());
        }
    }



}
