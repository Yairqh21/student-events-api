package com.devsz.studenteventsapi.service.impl;

import com.devsz.studenteventsapi.Utils.FirebaseUtils;
import com.devsz.studenteventsapi.dto.UserRequest;
import com.devsz.studenteventsapi.dto.UserResponse;
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

        return new UserResponse(
                userRecord.getUid(),
                userRecord.getDisplayName(),
                userRecord.getEmail(),
                user.getRole(),
                token,
                createdAt);
    }

    public void deleteUser(String uid) {
        try {
            FirebaseAuth.getInstance().deleteUser(uid);
        } catch (FirebaseAuthException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to delete user from Firebase Auth: " + e.getMessage());
        }
    }

    public Boolean checkUserByEmail(String email) throws FirebaseAuthException {

        UserRecord userRecord = FirebaseAuth.getInstance().getUserByEmail(email);
        if (userRecord != null) {
            throw new IllegalArgumentException("User not found");
        } 
        return  true;
    }



}
