package com.example.demo.Controller;

import com.example.demo.Servis.UserService;
import com.example.demo.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

        @CrossOrigin
        @GetMapping("get_id")
        @PreAuthorize("hasRole('ADMIN')")
        public ResponseEntity<?> getUser ( @RequestParam int user_id) {
            try {
                return ResponseEntity.ok(userService.getUser(user_id));
            } catch (Exception e) {
                return ResponseEntity.badRequest().body("Не найден: " + e.getMessage());
            }
        }

    @CrossOrigin
    @GetMapping("getAll")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getAllUser ( ) {
        try {
            return ResponseEntity.ok(userService.getAll());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Не найден: " + e.getMessage());
        }
    }

    @CrossOrigin
    @PostMapping("Create")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> Create(@Valid @RequestBody User user, BindingResult bindingResult) {
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
                return ResponseEntity.ok(userService.create(user));
            } catch (Exception e) {
                return ResponseEntity.badRequest().body(e.getMessage());
            }
        }

    }
}

