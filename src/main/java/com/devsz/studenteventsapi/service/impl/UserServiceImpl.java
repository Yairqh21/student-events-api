package com.devsz.studenteventsapi.service.impl;

import com.devsz.studenteventsapi.entity.UserEntity;
import com.devsz.studenteventsapi.firebase.FirebaseInitializer;
import com.devsz.studenteventsapi.service.IUserService;

import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends BaseServiceImpl<UserEntity, String> implements IUserService {

    public UserServiceImpl(FirebaseInitializer firebase) {
        super(firebase);
    }

    @Override
    protected Class<UserEntity> getEntityClass() {
        return UserEntity.class;
    }
}