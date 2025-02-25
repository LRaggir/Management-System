package com.example.demo.IServis;

import com.example.demo.model.User;
import org.springframework.http.ResponseEntity;

public interface IUser {
    ResponseEntity<?> getAll();
    ResponseEntity<?>  create(User user);
    ResponseEntity<?> authorization (User user);
    ResponseEntity<?> getUser(int userId);
}
