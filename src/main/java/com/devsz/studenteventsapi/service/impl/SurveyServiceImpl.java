package com.devsz.studenteventsapi.service.impl;

import com.devsz.studenteventsapi.entity.AnswerEntity;
import com.devsz.studenteventsapi.entity.QuestionEntity;
import com.devsz.studenteventsapi.entity.SurveyEntity;
import com.devsz.studenteventsapi.firebase.FirebaseInitializer;
import com.devsz.studenteventsapi.service.ISurveyService;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.cloud.firestore.WriteResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

@Service
@RequiredArgsConstructor
public class SurveyServiceImpl implements ISurveyService {

    private final FirebaseInitializer firebase;

    @Override
    public SurveyEntity save(SurveyEntity surveyEntity) throws Exception {
        Map<String, Object> data = getDocData(surveyEntity);

        CollectionReference collection = getCollection();
        DocumentReference documentReference = collection.document();
        ApiFuture<WriteResult> writeResultApiFuture = documentReference.set(data);

        try {
            if (writeResultApiFuture.get() != null) {
                surveyEntity.setId(documentReference.getId());
                return surveyEntity;
            }
        } catch (Exception e) {
            throw new RuntimeException("Error saving survey", e);
        }
        return null;
    }

    @Override
    public SurveyEntity update(SurveyEntity surveyEntity, String id) throws Exception {
        Map<String, Object> Data = getDocData(surveyEntity);
        ApiFuture<WriteResult> writeResultApiFuture = getCollection().document(id).set(Data);
        try {
            if (null != writeResultApiFuture.get()) {
                return surveyEntity;
            }
        } catch (Exception e) {
            return null;
        }
        return null;
    }

    @Override
    public List<SurveyEntity> readAll() throws Exception {
        List<SurveyEntity> response = new ArrayList<>();
        ApiFuture<QuerySnapshot> querySnapshotApiFuture = getCollection().get();
        try {
            for (DocumentSnapshot doc : querySnapshotApiFuture.get().getDocuments()) {
                SurveyEntity surveyEntity = doc.toObject(SurveyEntity.class);
                if (surveyEntity != null) {
                    surveyEntity.setId(doc.getId());
                    response.add(surveyEntity);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Error reading all surveys", e);
        }
        return response;
    }

    @Override
    public SurveyEntity readById(String id) throws Exception {
        ApiFuture<DocumentSnapshot> documentSnapshotApiFuture = getCollection().document(id).get();
        try {
            DocumentSnapshot documentSnapshot = documentSnapshotApiFuture.get();
            if (documentSnapshot.exists()) {
                SurveyEntity surveyEntity = documentSnapshot.toObject(SurveyEntity.class);
                if (surveyEntity != null) {
                    surveyEntity.setId(documentSnapshot.getId());
                    return surveyEntity;
                }
            }
        } catch (InterruptedException | ExecutionException e) {
            throw new Exception("Error getting survey by id: " + e.getMessage(), e);
        }
        return null;
    }

    @Override
    public void delete(String id) throws Exception {
        ApiFuture<WriteResult> writeResultApiFuture = getCollection().document(id).delete();
    }

    // guardamos los datos de la encuesta

    public List<QuestionEntity> getQuestions(String surveyId) throws Exception {
        List<QuestionEntity> questions = new ArrayList<>();
        ApiFuture<QuerySnapshot> querySnapshotApiFuture = getCollection().document(surveyId).collection("questions")
                .get();
        try {
            for (DocumentSnapshot doc : querySnapshotApiFuture.get().getDocuments()) {
                QuestionEntity question = doc.toObject(QuestionEntity.class);
                question.setId(doc.getId());
                questions.add(question);
            }
            return questions;
        } catch (Exception e) {
            throw new Exception("Error getting questions for survey with id " + surveyId + ": " + e.getMessage());
        }
    }

    public List<AnswerEntity> getAnswers(String surveyId) throws Exception {
        List<AnswerEntity> answers = new ArrayList<>();
        ApiFuture<QuerySnapshot> querySnapshotApiFuture = getCollection().document(surveyId).collection("answers")
                .get();
        try {
            for (DocumentSnapshot doc : querySnapshotApiFuture.get().getDocuments()) {
                AnswerEntity answer = doc.toObject(AnswerEntity.class);
                answer.setId(doc.getId());
                answers.add(answer);
            }
            return answers;
        } catch (Exception e) {
            throw new Exception("Error getting answers for survey with id " + surveyId + ": " + e.getMessage());
        }
    }

    private CollectionReference getCollection() {
        return firebase.getFirestore().collection("surveys");
    }

    private Map<String, Object> getDocData(SurveyEntity surveyEntity) {
        Map<String, Object> Data = new HashMap<>();
        Data.put("surveyName", surveyEntity.getSurveyName());
        Data.put("isActive", surveyEntity.getIsActive());
        return Data;
    }
}
