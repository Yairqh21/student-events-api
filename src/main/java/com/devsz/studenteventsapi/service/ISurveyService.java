package com.devsz.studenteventsapi.service;

import java.util.concurrent.ExecutionException;

import com.devsz.studenteventsapi.dto.PathRequest;
import com.devsz.studenteventsapi.entity.SurveyEntity;

public interface ISurveyService extends ICRUD<SurveyEntity, String>{

    SurveyEntity readByEventId(PathRequest pathRequest, String eventId) throws ExecutionException, InterruptedException;
}
