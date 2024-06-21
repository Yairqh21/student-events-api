package com.devsz.studenteventsapi.service;

import com.devsz.studenteventsapi.entity.AnswerEntity;

import java.util.List;

public interface IAnswerService {

    AnswerEntity saveAnswer(String surveyId, AnswerEntity answer) throws Exception;
    List<AnswerEntity> readAll(String surveyId) throws Exception;
    AnswerEntity readById(String surveyId, AnswerEntity answer) throws Exception;
    void delete(String surveyId, AnswerEntity answer) throws Exception;
}
