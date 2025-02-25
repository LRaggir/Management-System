package com.example.demo.IServis;

import com.example.demo.model.User;
import com.example.demo.model.Task;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

public interface ITask {
    public ResponseEntity<?> Create(Task Tas);
    ResponseEntity<?> Delete(int id);
    ResponseEntity<?> Edit(Task Tas, User currentUser);
    ResponseEntity<?> getAlltask(int user_id);
    ResponseEntity<?> gettask(int task_id);
    public ResponseEntity<?> getA(int userId, Pageable pageable) ;
}