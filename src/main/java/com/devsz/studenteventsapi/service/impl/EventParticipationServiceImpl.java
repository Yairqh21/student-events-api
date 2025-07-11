package com.devsz.studenteventsapi.service.impl;

import org.springframework.stereotype.Service;

import com.devsz.studenteventsapi.entity.EventParticipationEntity;
import com.devsz.studenteventsapi.firebase.FirebaseInitializer;
import com.devsz.studenteventsapi.service.IEventParticipationService;

@Service
public class EventParticipationServiceImpl extends BaseServiceImpl<EventParticipationEntity, String> implements IEventParticipationService {

    public EventParticipationServiceImpl(FirebaseInitializer firebase) {
        super(firebase);
    }

    @Override
    protected Class<EventParticipationEntity> getEntityClass() {
        return EventParticipationEntity.class;
    }
}

