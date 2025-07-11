package com.devsz.studenteventsapi.service.impl;

import com.devsz.studenteventsapi.dto.PathRequest;
import com.devsz.studenteventsapi.entity.EventEntity;
import com.devsz.studenteventsapi.firebase.FirebaseInitializer;

import java.sql.Timestamp;
import java.util.List;
import java.util.concurrent.ExecutionException;
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

    @Override
    public List<EventEntity> readAllPaginated(PathRequest pathRequest, int limit, Timestamp startAfter)
            throws ExecutionException, InterruptedException {
        return super.readAllPaginated(pathRequest, limit, startAfter);
    }

    @Override
    public List<EventEntity> readAllOfUserCreated(PathRequest pathRequest, String userId, String field, int limit,
            Timestamp startAfter) throws ExecutionException, InterruptedException {
        return super.readAllOfUserCreated(pathRequest, userId, field, limit, startAfter);
    }

}
