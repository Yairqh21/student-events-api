package com.devsz.studenteventsapi.service.impl;

import com.devsz.studenteventsapi.entity.EventEntity;
import com.devsz.studenteventsapi.firebase.FirebaseInitializer;
import org.springframework.stereotype.Service;
import com.devsz.studenteventsapi.service.IEventService;


@Service
public class EventServiceImpl extends BaseServiceImpl<EventEntity, String> implements IEventService {

    public EventServiceImpl(FirebaseInitializer firebase) {
        super(firebase);
    }

    @Override
    protected Class<EventEntity> getEntityClass() {
        return EventEntity.class;
    }
}
