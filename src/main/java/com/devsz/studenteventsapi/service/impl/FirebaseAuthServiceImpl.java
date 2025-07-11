package com.devsz.studenteventsapi.service.impl;

import com.devsz.studenteventsapi.Utils.FirebaseUtils;
import com.devsz.studenteventsapi.dto.UserRequest;
import com.devsz.studenteventsapi.dto.UserResponse;
import com.google.cloud.Timestamp;
import com.google.firebase.auth.AuthErrorCode;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.UserRecord;

import org.springframework.stereotype.Service;

@Service
public class FirebaseAuthServiceImpl {

    public UserResponse registerUserAuth(UserRequest user) throws FirebaseAuthException {
        FirebaseUtils.validateRoles(user.getRole());

        UserRecord.CreateRequest request = new UserRecord.CreateRequest()
                .setEmail(user.getEmail())
                .setPassword(user.getPassword())
                .setDisplayName(user.getUsername());

        // Crear usuario en Firebase
        UserRecord userRecord = FirebaseAuth.getInstance().createUser(request);

        final Long createdAt = userRecord.getUserMetadata().getCreationTimestamp();
        final String token = FirebaseUtils.generateCustomToken(user, userRecord.getUid());

        UserRequest userRequest = UserRequest.builder()
                .id(userRecord.getUid())
                .email(userRecord.getEmail())
                .username(userRecord.getDisplayName())
                .role(user.getRole())
                .createdAt(Timestamp.ofTimeSecondsAndNanos(createdAt / 1000, 0))
                .build();
        return new UserResponse(userRequest, token);
    }

    public void deleteUser(String uid) {
        try {
            FirebaseAuth.getInstance().deleteUser(uid);
        } catch (FirebaseAuthException e) {
            throw new RuntimeException("Failed to delete user from Firebase Auth: " + e.getMessage());
        }
    }

    public Boolean checkUserByEmail(String email) {
        try {
            FirebaseAuth.getInstance().getUserByEmail(email);
            return true;
        } catch (FirebaseAuthException e) {
            if (e.getAuthErrorCode().equals(AuthErrorCode.USER_NOT_FOUND)) {
                return false;
            }
            throw new RuntimeException("Error checking user by email: " + e.getMessage());
        }
    }

}
