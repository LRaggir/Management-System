package com.example.demo.Servis;

import com.example.demo.IServis.IUser;
import com.example.demo.Security.JwtUtil;
import com.example.demo.model.Role;
import com.example.demo.model.User;
import com.example.demo.repo.UserRepository;
import com.example.demo.repo.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService implements IUser {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TaskRepository Task;
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private UserDetailsService customUserDetailsService;

        public ResponseEntity<?> getAll() {
            try {
                Iterable<User> users = userRepository.findAll();
                return ResponseEntity.ok(users);
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("Ошибка при получении пользователей: " + e.getMessage());
            }
        }

        public ResponseEntity<?> create(User user) {
            try {
                Optional<User> optionalUser = userRepository.findByEmail(user.getEmail());

                if (optionalUser.isPresent()) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                            .body("Пользователь с таким email уже существует");
                } else {
                    userRepository.save(user);
                    return ResponseEntity.status(HttpStatus.CREATED).body("Пользователь успешно создан");
                }
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("Ошибка при создании пользователя: " + e.getMessage());
            }
        }

        public ResponseEntity<?> authorization(User user) {
            try {

                Optional<User> optionalUser = userRepository.findByEmail(user.getEmail());
                if (!optionalUser.isPresent()) {
                    return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                            .body("Неверный логин или пароль");
                } else {
                    User existingUser = optionalUser.get();
                    if (existingUser.getPassword().equals(user.getPassword())) {
                        UserDetails userDetails = customUserDetailsService.loadUserByUsername(user.getEmail());
                        String token = jwtUtil.generateToken(userDetails, existingUser.getId());
                        Authentication authentication = new UsernamePasswordAuthenticationToken(
                                userDetails,
                                null,
                                userDetails.getAuthorities()
                        );
                        SecurityContextHolder.getContext().setAuthentication(authentication);
                        return ResponseEntity.ok(token);
                    } else {
                        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                                .body("Неверный логин или пароль");
                    }
                }
            } catch (UsernameNotFoundException e) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Пользователь не найден");
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("Ошибка при авторизации: " + e.getMessage());
            }
        }

        public ResponseEntity<?> getUser(int userId) {
            try {
                Optional<User> userOptional = userRepository.findById(userId);
                if (userOptional.isPresent()) {
                    return ResponseEntity.ok(userOptional.get());
                } else {
                    return ResponseEntity.status(HttpStatus.NOT_FOUND)
                            .body("Пользователь с ID " + userId + " не найден");
                }
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("Ошибка при получении пользователя: " + e.getMessage());
            }
        }
    }


