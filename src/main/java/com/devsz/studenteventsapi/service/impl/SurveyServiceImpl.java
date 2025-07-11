package com.devsz.studenteventsapi.service.impl;

import java.util.concurrent.ExecutionException;
import org.springframework.stereotype.Service;

import com.devsz.studenteventsapi.dto.PathRequest;
import com.devsz.studenteventsapi.entity.SurveyEntity;
import com.devsz.studenteventsapi.firebase.FirebaseInitializer;
import com.devsz.studenteventsapi.service.ISurveyService;

@Service
public class SurveyServiceImpl extends BaseServiceImpl<SurveyEntity, String> implements ISurveyService {

    public SurveyServiceImpl(FirebaseInitializer firebase) {
        super(firebase);
    }

    @Override
    protected Class<SurveyEntity> getEntityClass() {
        return SurveyEntity.class;
    }

    @Override
    public SurveyEntity readByEventId(PathRequest pathRequest, String eventId)
            throws ExecutionException, InterruptedException {

        return super.readAll(pathRequest).stream().filter(survey -> survey.getEventId().equals(eventId)).findFirst()
                .orElse(null);

    }

}
