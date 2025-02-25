package com.example.demo.Servis;

import com.example.demo.IServis.ITask;
import com.example.demo.model.*;
import com.example.demo.repo.ReviewRepository;
import com.example.demo.repo.TaskRepository;
import com.example.demo.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import javax.persistence.EntityNotFoundException;
import java.util.*;

@Service
public class TaskService implements ITask {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TaskRepository taskRepository;
    @Autowired
    private ReviewRepository reviewRepository;

    public  ResponseEntity<?> Create(Task tas) {
        Optional<User> optionalUser = userRepository.findById(tas.getAuthor());
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            tas.setAuthor(user.getId());
            taskRepository.save(tas);
            return ResponseEntity.status(HttpStatus.CREATED).body("Задача успешно создана");
        } else {
            throw new EntityNotFoundException("Пользователь с id " + tas.getAuthor() + " не найден");
        }
    }

    public  ResponseEntity<?> Delete(int id) {
        if (taskRepository.existsById(id)) {
            taskRepository.deleteById(id);
            Iterable<Review>  reviews = reviewRepository.findAll();
            reviews.forEach(review -> {
                if (review.getTaskId() == id ) {
                    reviewRepository.delete(review);
                }
            });
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Задача успешно удалена");
        } else {
            throw new EntityNotFoundException("Задача с id " + id + " не найдена");
        }
    }

    public  ResponseEntity<?> Edit(Task tas, User currentUser) {
        Optional<Task> optionalTask = taskRepository.findById(tas.getId());
        if (optionalTask.isEmpty()) {
            throw new EntityNotFoundException("Задача с id " + tas.getId() + " не найдена");
        }

        Task existingTask = optionalTask.get();
        if (currentUser.getRole().equals(Role.ADMIN)) {
            existingTask.setTitle(tas.getTitle());
            existingTask.setDescription(tas.getDescription());
            existingTask.setPriority(tas.getPriority());
            existingTask.setStatus(tas.getStatus());
            existingTask.setPerformer(tas.getPerformer());
        } else if (currentUser.getId().equals(existingTask.getPerformer())) {
            existingTask.setStatus(tas.getStatus());
        } else {
            throw new AccessDeniedException("У вас нет прав на изменение этой задачи");
        }

        taskRepository.save(existingTask);
        return ResponseEntity.status(HttpStatus.OK).body("Задача успешно обновлена");
    }

    public ResponseEntity<?> getAll() {
        Iterable<Task> tasks = taskRepository.findAll();
        return ResponseEntity.status(HttpStatus.OK).body(tasks);
    }

    public  ResponseEntity<?> getAlltask(int userId) {
        List<Task> tasks = new ArrayList<>();
        Iterable<Task> allTasks = taskRepository.findAll();
        for (Task task : allTasks) {
            if (task.getPerformer() == userId) {
                tasks.add(task);
            }
        }
        return ResponseEntity.status(HttpStatus.OK).body(tasks);
    }

    public  ResponseEntity<?> gettask(int taskId) {
        Optional<Task> taskOptional = taskRepository.findById(taskId);
        return taskOptional
                .map(task -> ResponseEntity.status(HttpStatus.OK).body(task))
                .orElseThrow(() -> new EntityNotFoundException("Задача с id " + taskId + " не найдена"));
    }

    public ResponseEntity<List<TaskWithReviewsDTO>> getA(int userId, Pageable pageable) {
        List<TaskWithReviewsDTO> taskList = new ArrayList<>();

        Iterable<Task> allTasks = taskRepository.findAll();
        for (Task task : allTasks) {
            if (task.getPerformer() == userId) {
                Page<Review> reviewsPage = reviewRepository.findByTaskId(task.getId(), pageable);
                List<Review> reviews = reviewsPage.getContent();
                taskList.add(new TaskWithReviewsDTO(task, reviews));
            }
        }
        return ResponseEntity.ok(taskList);
    }

}
