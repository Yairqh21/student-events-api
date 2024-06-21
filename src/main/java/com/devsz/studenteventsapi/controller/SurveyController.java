package com.devsz.studenteventsapi.controller;

import com.devsz.studenteventsapi.entity.AnswerEntity;
import com.devsz.studenteventsapi.entity.QuestionEntity;
import com.devsz.studenteventsapi.entity.SurveyEntity;
import com.devsz.studenteventsapi.service.IAnswerService;
import com.devsz.studenteventsapi.service.IQuestionService;
import com.devsz.studenteventsapi.service.ISurveyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/surveys")
@RequiredArgsConstructor
public class SurveyController {

    private final ISurveyService service;
    private final IQuestionService questionService;
    private final IAnswerService answerService;

    @GetMapping
    public ResponseEntity<List<SurveyEntity>> readAll() throws Exception {
        return new ResponseEntity<>(service.readAll(), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<SurveyEntity> create(@RequestBody SurveyEntity surveyEntity) {
        try {
            SurveyEntity createdSurvey = service.save(surveyEntity);
            return new ResponseEntity<>(createdSurvey, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<SurveyEntity> update(@PathVariable(value = "id") String id,
            @RequestBody SurveyEntity surveyEntity) throws Exception {
        return new ResponseEntity<>(service.update(surveyEntity, id), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable(value = "id") String id) throws Exception {
        service.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SurveyEntity> readById(@PathVariable(value = "id") String id) throws Exception {
        return new ResponseEntity<>(service.readById(id), HttpStatus.OK);
    }

    @PostMapping("/{id}/questions")
    public ResponseEntity<List<QuestionEntity>> createQuestions(@PathVariable(value = "id") String surveyId,
            @RequestBody List<QuestionEntity> questionEntities) throws Exception {
        List<QuestionEntity> savedQuestions = questionService.saveAll(surveyId, questionEntities);
        return new ResponseEntity<>(savedQuestions, HttpStatus.CREATED);
    }

    @PutMapping("/{id}/questions")
    public ResponseEntity<List<QuestionEntity>> updateQuestions(@PathVariable(value = "id") String surveyId,
            @RequestBody List<QuestionEntity> questionEntities) throws Exception {
        List<QuestionEntity> updatedQuestions = questionService.update(surveyId, questionEntities);
        return new ResponseEntity<>(updatedQuestions, HttpStatus.OK);
    }

    @GetMapping("/{id}/questions")
    public ResponseEntity<List<QuestionEntity>> readQuestions(@PathVariable(value = "id") String surveyId)
            throws Exception {
        return new ResponseEntity<>(questionService.readAll(surveyId), HttpStatus.OK);
    }

    // métodos relacionados a AnswerEntity

    // Create respuestas
    @PostMapping("/{id}/answers")
    public ResponseEntity<AnswerEntity> createAnswers(@PathVariable(value = "id") String surveyId,
            @RequestBody AnswerEntity answerEntities) throws Exception {
        AnswerEntity savedAnswers = answerService.saveAnswer(surveyId, answerEntities);
        return new ResponseEntity<>(savedAnswers, HttpStatus.CREATED);
    }

    // Delete respuesta
    @DeleteMapping("/{surveyId}/answers/{answerId}")
    public ResponseEntity<Void> deleteAnswer(@PathVariable String surveyId, @PathVariable String answerId)
            throws Exception {
        AnswerEntity answer = new AnswerEntity();
        answer.setId(answerId);
        answerService.delete(surveyId, answer);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    // Read respuesta por ID de evento y ID de usuario
    @GetMapping("/{surveyId}/answers/{eventId}/{userId}")
    public ResponseEntity<AnswerEntity> getAnswerByEventAndUser(@PathVariable String surveyId,
            @PathVariable String eventId, @PathVariable String userId) throws Exception {
        AnswerEntity answer = new AnswerEntity();
        answer.setEventId(eventId);
        answer.setUserId(userId);
        AnswerEntity retrievedAnswer = answerService.readById(surveyId, answer);
        return new ResponseEntity<>(retrievedAnswer, HttpStatus.OK);
    }

    // ReadAll respuestas
    @GetMapping("/{id}/answers")
    public ResponseEntity<List<AnswerEntity>> getAllAnswers(@PathVariable(value = "id") String surveyId)
            throws Exception {
        List<AnswerEntity> answers = answerService.readAll(surveyId);
        return new ResponseEntity<>(answers, HttpStatus.OK);
    }
}
