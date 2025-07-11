package com.devsz.studenteventsapi.service.impl;

import org.springframework.stereotype.Service;

import com.devsz.studenteventsapi.entity.AnswerEntity;
import com.devsz.studenteventsapi.firebase.FirebaseInitializer;
import com.devsz.studenteventsapi.service.IResponseService;

@Service
public class ResponseServiceImpl extends BaseServiceImpl<AnswerEntity, String> implements IResponseService {

    public ResponseServiceImpl(FirebaseInitializer firebase) {
        super(firebase);
    }

    @Override
    protected Class<AnswerEntity> getEntityClass() {
        return AnswerEntity.class;
    }

}
