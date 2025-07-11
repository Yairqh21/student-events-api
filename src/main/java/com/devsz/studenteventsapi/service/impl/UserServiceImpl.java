package com.devsz.studenteventsapi.service.impl;

import com.devsz.studenteventsapi.dto.UserRequest;
import com.devsz.studenteventsapi.entity.UserEntity;
import com.devsz.studenteventsapi.firebase.FirebaseInitializer;
import com.devsz.studenteventsapi.service.IUserService;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.WriteResult;

import java.util.concurrent.ExecutionException;

import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends BaseServiceImpl<UserEntity, String> implements IUserService {

    private final FirebaseInitializer fireInit;

    public UserServiceImpl(FirebaseInitializer firebase) {
        super(firebase);
        this.fireInit = firebase;
    }

    @Override
    protected Class<UserEntity> getEntityClass() {
        return UserEntity.class;
    }

    @Override
    public Boolean registerUser(UserRequest userRequest) throws ExecutionException, InterruptedException {
        ApiFuture<WriteResult> writeResultApiFuture = fireInit.getFirestore().collection("users")
                .document(userRequest.getId()).set(userRequest);
        WriteResult writeResult = writeResultApiFuture.get();
        return writeResult != null;
    }
}