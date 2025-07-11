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
import com.google.firebase.auth.FirebaseAuth;
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
            boolean isUserSaved = saveUserInFirestore(userResponse);
            // si Firestore falla
            if (!isUserSaved) {
                firebaseAuthService.deleteUser(userResponse.user().getId());
                return ResponseEntity.status(500).body(
                        Map.of("error", "User registration failed"));
            }

            return ResponseEntity.status(201).body(userResponse);
        } catch (FirebaseAuthException e) {
            return ResponseEntity.status(400).body(
                    Map.of("error", "Error with Firebase Auth: " + e.getMessage()));
        }
    }

    @PostMapping("/check-account")
    public ResponseEntity<Boolean> checkEmailExists(@RequestBody EmailRequest emailRequest) {
        try {
            boolean exists = firebaseAuthService.checkUserByEmail(emailRequest.email());
            return ResponseEntity.ok(exists);
        } catch (RuntimeException e) {
            return ResponseEntity.status(400).body(false);
        }
    }

    @GetMapping("/check-token")
    public ResponseEntity<Void> validateToken(@RequestHeader("Authorization") String authHeader) {
        String token = authHeader.replace("Bearer ", "");
        try {
            FirebaseAuth.getInstance().verifyIdToken(token);
            return ResponseEntity.ok().build();
        } catch (FirebaseAuthException e) {
            return ResponseEntity.status(401).build();
        }
    }

    private boolean saveUserInFirestore(UserResponse userResponse) throws Exception {
        UserRequest updatedUserDTO = UserRequest.builder()
                .id(userResponse.user().getId())
                .email(userResponse.user().getEmail())
                .username(userResponse.user().getUsername())
                .role(userResponse.user().getRole())
                .createdAt(userResponse.user().getCreatedAt())
                .build();
        return userService.registerUser(updatedUserDTO);
    }
}
