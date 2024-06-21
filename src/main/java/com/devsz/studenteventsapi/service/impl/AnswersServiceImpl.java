package com.devsz.studenteventsapi.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.devsz.studenteventsapi.entity.AnswerEntity;
import com.devsz.studenteventsapi.firebase.FirebaseInitializer;
import com.devsz.studenteventsapi.service.IAnswerService;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.cloud.firestore.WriteResult;

import lombok.RequiredArgsConstructor;

import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class AnswersServiceImpl implements IAnswerService {

    private final FirebaseInitializer firebase;

    private CollectionReference getCollection(String surveyId) {
        return firebase.getFirestore().collection("surveys").document(surveyId).collection("answers");
    }

    private Map<String, Object> getDocData(AnswerEntity answerEntity) {
        Map<String, Object> data = new HashMap<>();
        data.put("eventId", answerEntity.getEventId());
        data.put("userId", answerEntity.getUserId());
        data.put("answers", answerEntity.getAnswers());
        return data;
    }

    @Override
    public AnswerEntity saveAnswer(String surveyId, AnswerEntity answer) throws Exception {
        Map<String, Object> data = getDocData(answer);
        ApiFuture<WriteResult> writeResult = getCollection(surveyId).document().set(data);
        if (writeResult != null) {
            return answer;
        }
        return null;
    }

    @Override
    public List<AnswerEntity> readAll(String surveyId) throws Exception {
        List<AnswerEntity> answers = new ArrayList<>();
        CollectionReference answersCollection = getCollection(surveyId);

        ApiFuture<QuerySnapshot> querySnapshot = answersCollection.get();
        List<QueryDocumentSnapshot> documents = querySnapshot.get().getDocuments();
        for (QueryDocumentSnapshot document : documents) {
            AnswerEntity answer = document.toObject(AnswerEntity.class);
            answer.setId(document.getId());
            answers.add(answer);
        }
        return answers;
    }

    @Override
    public AnswerEntity readById(String surveyId, AnswerEntity answer) throws Exception {
        ApiFuture<QuerySnapshot> querySnapshot = getCollection(surveyId)
                .whereEqualTo("eventId", answer.getEventId())
                .whereEqualTo("userId", answer.getUserId()).get();
        try {
            QueryDocumentSnapshot document = querySnapshot.get().getDocuments().get(0);
            AnswerEntity answerEntity = document.toObject(AnswerEntity.class);
            answerEntity.setId(document.getId());
            return answerEntity;
        } catch (Exception e) {
            // Handle the exception appropriately
            throw new RuntimeException("Error reading answer with eventId " + answer.getEventId() + " and userId " + answer.getUserId(), e);
        }
    }

    @Override
    public void delete(String surveyId, AnswerEntity answer) throws Exception {
        ApiFuture<WriteResult> writeResult = getCollection(surveyId).document(answer.getId()).delete();
    }
}
