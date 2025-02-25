package com.example.demo.Controller;

import com.example.demo.Security.CustomUserDetails;
import com.example.demo.Servis.TaskService;
import com.example.demo.model.User;
import com.example.demo.model.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.List;

@RestController
public class TaskController {

    @Autowired
    private TaskService taskRepository;
    @CrossOrigin
    @PostMapping("/create")
    @PreAuthorize("hasRole('ADMIN')")

    public  ResponseEntity<?> createTask(@Valid @RequestBody Task task, BindingResult bindingResult) {
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
                taskRepository.Create( task);
                return ResponseEntity.status(HttpStatus.OK)
                        .body("{\"message\": \"Задача создана успешно\"}");
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(e.getMessage());
            }
        }
    }
    @CrossOrigin
    @DeleteMapping("/delete")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteTask(@RequestParam("task_id") @Min(value = 1, message = "ID задачи должен быть больше нуля") int taskId) {
        try {
            taskRepository.Delete(taskId);
            return ResponseEntity.status(HttpStatus.OK)
                    .body("{\"message\": \"Задача удалена успешно\"}");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("{\"error\": \"Ошибка удаления задачи\"}");
        }
    }
    @CrossOrigin
    @PostMapping("/edit")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public  ResponseEntity<?> editTask(@Valid @RequestBody Task task, BindingResult bindingResult) {
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
                Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
                CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
                User user = userDetails.getUser();
                taskRepository.Edit(task,user);
                return ResponseEntity.status(HttpStatus.OK)
                        .body(taskRepository.Edit(task,user));

            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(e.getMessage());

            }
        }
    }
    @CrossOrigin
    @GetMapping("/get")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getAllTasks() {
        try {

            return ResponseEntity.ok(taskRepository.getAll());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @CrossOrigin
    @GetMapping("/getbyuser")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> getAllTask(@RequestParam int user_id) {
        try {

            return ResponseEntity.ok(taskRepository.getAlltask(user_id));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @CrossOrigin
    @GetMapping("/getbytask_id")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<?> gettask_id(@RequestParam int task_id) {
        try {
            return ResponseEntity.ok( taskRepository.gettask(task_id));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body( e.getMessage());
        }
    }


    @GetMapping("/byAssignee")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<?> getReviewsByAssignee(
            @RequestParam int user_id,
            @RequestParam int page,
            @RequestParam int size) {


        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(taskRepository.getA(user_id,pageable));
    }

}
