package com.devsz.studenteventsapi.service;

import java.sql.Timestamp;
import java.util.List;
import java.util.concurrent.ExecutionException;

import com.devsz.studenteventsapi.dto.PathRequest;
import com.devsz.studenteventsapi.entity.EventEntity;

public interface IEventService extends ICRUD<EventEntity, String>{

    List<EventEntity> readAllPaginated(PathRequest pathRequest, int limit, Timestamp startAfter) throws ExecutionException, InterruptedException;
    List<EventEntity> readAllOfUserCreated(PathRequest pathRequest, String userId, String field, int limit, Timestamp startAfter)  throws ExecutionException, InterruptedException;
}
