package com.devsz.studenteventsapi.service;

import com.devsz.studenteventsapi.entity.QuestionEntity;
import java.util.List;

public interface IQuestionService {
     List<QuestionEntity> saveAll(String surveyId, List<QuestionEntity> questions) throws Exception;
     List<QuestionEntity> update(String surveyId, List<QuestionEntity> questions) throws Exception;
     List<QuestionEntity> readAll(String surveyId) throws Exception;
     QuestionEntity readById(String surveyId, String id) throws Exception;
     void delete(String surveyId, String id) throws Exception;
}
