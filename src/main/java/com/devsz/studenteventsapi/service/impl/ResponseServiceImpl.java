package com.devsz.studenteventsapi.service.impl;

import org.springframework.stereotype.Service;

import com.devsz.studenteventsapi.entity.ResponsesEntity;
import com.devsz.studenteventsapi.firebase.FirebaseInitializer;
import com.devsz.studenteventsapi.service.IResponseService;

@Service
public class ResponseServiceImpl extends BaseServiceImpl<ResponsesEntity, String> implements IResponseService {

    public ResponseServiceImpl(FirebaseInitializer firebase) {
        super(firebase);
    }

    @Override
    protected Class<ResponsesEntity> getEntityClass() {
        return ResponsesEntity.class;
    }

}
