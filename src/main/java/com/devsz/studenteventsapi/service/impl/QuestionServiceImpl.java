package com.devsz.studenteventsapi.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;

import org.springframework.stereotype.Service;

import com.devsz.studenteventsapi.entity.QuestionEntity;
import com.devsz.studenteventsapi.firebase.FirebaseInitializer;
import com.devsz.studenteventsapi.service.IQuestionService;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.cloud.firestore.WriteResult;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class QuestionServiceImpl implements IQuestionService {

     private final FirebaseInitializer firebase;

     @Override
     public List<QuestionEntity> saveAll(String surveyId, List<QuestionEntity> questions) throws Exception {
          List<QuestionEntity> savedQuestions = new ArrayList<>();
          CollectionReference questionsCollection = getCollection(surveyId);
          
          for (QuestionEntity question : questions) {
               Map<String, Object> data = getDocData(question);
               ApiFuture<WriteResult> writeResult = questionsCollection.document().set(data);
               if (writeResult != null) {
                    savedQuestions.add(question);
               }
          }
          return savedQuestions;
     }

     @Override
     public List<QuestionEntity> update(String surveyId, List<QuestionEntity> questions) throws Exception {
         List<QuestionEntity> updatedQuestions = new ArrayList<>();
         CollectionReference questionsCollection = getCollection(surveyId);
         
         for (QuestionEntity question : questions) {
             Map<String, Object> data = getDocData(question);
             ApiFuture<WriteResult> writeResult = questionsCollection.document(question.getId()).set(data);
             try {
                 if (null != writeResult.get()) {
                     updatedQuestions.add(question);
                 }
             } catch (Exception e) {
                 // Handle the exception appropriately
                 throw new RuntimeException("Error updating question with ID " + question.getId(), e);
             }
         }
         return updatedQuestions;
     }

     @Override
     public List<QuestionEntity> readAll(String surveyId) throws Exception {
          List<QuestionEntity> questions = new ArrayList<>();
          ApiFuture<QuerySnapshot> querySnapshotApiFuture = getCollection(surveyId).get();
          try {
               for (DocumentSnapshot doc : querySnapshotApiFuture.get().getDocuments()) {
                    QuestionEntity question = doc.toObject(QuestionEntity.class);
                    question.setId(doc.getId());
                    questions.add(question);
               }
               return questions;
          } catch (Exception e) {
               return null;
          }
     }

     @Override
     public QuestionEntity readById(String surveyId, String id) throws Exception {
          ApiFuture<DocumentSnapshot> documentSnapshotApiFuture = getCollection(surveyId).document(id).get();
          try {
               DocumentSnapshot documentSnapshot = documentSnapshotApiFuture.get();
               QuestionEntity question = documentSnapshot.toObject(QuestionEntity.class);
               return question;
          } catch (Exception e) {
               throw new RuntimeException("Error reading question with ID " + id, e);
          }
     }

     @Override
     public void delete(String surveyId, String id) throws Exception {
          ApiFuture<WriteResult> writeResultApiFuture = getCollection(surveyId).document(id).delete();
     }

     private CollectionReference getCollection(String surveyId) {
          return firebase.getFirestore().collection("surveys").document(surveyId).collection("questions");
     }

     private Map<String, Object> getDocData(QuestionEntity question) {
          Map<String, Object> docData = new HashMap<>();
          docData.put("text", question.getText());
          return docData;
     }

}
