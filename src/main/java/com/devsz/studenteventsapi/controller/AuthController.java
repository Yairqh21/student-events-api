package com.devsz.studenteventsapi.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.devsz.studenteventsapi.dto.EmailRequest;
import com.devsz.studenteventsapi.dto.UserRequest;
import com.devsz.studenteventsapi.dto.UserResponse;
import com.devsz.studenteventsapi.service.IUserService;
import com.devsz.studenteventsapi.service.impl.FirebaseAuthServiceImpl;
import com.google.cloud.Timestamp;
import com.google.firebase.auth.FirebaseAuthException;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private FirebaseAuthServiceImpl firebaseAuthService;

    @Autowired
    private IUserService userService;

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody UserRequest userDTO) throws Exception {
        try {
            UserResponse userResponse = firebaseAuthService.registerUserAuth(userDTO);

            boolean isUserSaved = saveUserInFirestore(userDTO, userResponse);
            // si Firestore falla
            if (!isUserSaved) {
                firebaseAuthService.deleteUser(userResponse.id());
                return ResponseEntity.status(500).body(
                        Map.of("error", "User registration failed"));
            }

            return ResponseEntity.status(201).body(userResponse);
        } catch (FirebaseAuthException e) {
            return ResponseEntity.status(400).body(
                    Map.of("error", "Error with Firebase Auth: " + e.getMessage()));
        }
    }

    @PostMapping("/email-exists")
    public ResponseEntity<Boolean> checkEmailExists(@RequestBody EmailRequest emailRequest) {
        try {
            return ResponseEntity.ok(firebaseAuthService.checkUserByEmail(emailRequest.email()));
        } catch (Exception e) {
            return ResponseEntity.status(401).body(false);
        }
    }

    private boolean saveUserInFirestore(UserRequest userDTO, UserResponse userResponse) throws Exception {
        UserRequest updatedUserDTO = UserRequest.builder()
                .id(userResponse.id())
                .email(userDTO.getEmail())
                .username(userDTO.getUsername())
                .role(userDTO.getRole())
                .createdAt(Timestamp.ofTimeSecondsAndNanos(userResponse.createdAt() / 1000, 0))
                .build();

        return userService.registerUser(updatedUserDTO);
    }
}
