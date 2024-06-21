package com.devsz.studenteventsapi.service.impl;

import com.devsz.studenteventsapi.entity.AssistanceEntity;
import com.devsz.studenteventsapi.firebase.FirebaseInitializer;
import com.devsz.studenteventsapi.service.IAssistanceService;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.CollectionReference;
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
public class AssistanceServiceImpl implements IAssistanceService {


    // @Autowired
    private final FirebaseInitializer firebase;

    @Override
    public AssistanceEntity save(AssistanceEntity assistanceEntity) throws Exception {
        Map<String, Object> Data = getDocData(assistanceEntity);

        ApiFuture<WriteResult> writeResultApiFuture = getCollection().document().create(Data);

        try {
            if (null != writeResultApiFuture.get()) {
                return assistanceEntity;
            }
        } catch (Exception e) {
            return null;
        }
        return null;
    }


    @Override
    public AssistanceEntity update(AssistanceEntity assistanceEntity, String id) throws Exception {
        Map<String, Object> Data = getDocData(assistanceEntity);
        ApiFuture<WriteResult> writeResultApiFuture = getCollection().document(id).set(Data);
        try {
            if (null != writeResultApiFuture.get()) {
                return assistanceEntity;
            }
        } catch (Exception e) {
            return null;
        }
        return null;
    }

    @Override
    public List<AssistanceEntity> readAll() throws Exception {
        List<AssistanceEntity> response = new ArrayList<>();
        AssistanceEntity assistanceEntity;

        ApiFuture<QuerySnapshot> querySnapshotApiFuture = getCollection().get();
        try {
            for (DocumentSnapshot doc : querySnapshotApiFuture.get().getDocuments()) {
                assistanceEntity = doc.toObject(AssistanceEntity.class);
                assistanceEntity.setId(doc.getId());
                response.add(assistanceEntity);

            }
            return response;
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public AssistanceEntity readById(String id) throws Exception {
        ApiFuture<DocumentSnapshot> documentSnapshotApiFuture = getCollection().document(id).get();
        try {
            DocumentSnapshot documentSnapshot = documentSnapshotApiFuture.get();
            if (documentSnapshot.exists()) {
                AssistanceEntity assistanceEntity = documentSnapshot.toObject(AssistanceEntity.class);
                assistanceEntity.setId(documentSnapshot.getId());
                return assistanceEntity;
            } else {
                return null;
            }
        } catch (InterruptedException | ExecutionException e) {
            throw new Exception("Error getting event by id: " + e.getMessage());
        }
    }

    @Override
    public void delete(String id) throws Exception {
        ApiFuture<WriteResult> writeResultApiFuture = getCollection().document(id).delete();
    }
    private CollectionReference getCollection() {
        return firebase.getFirestore().collection("assistances");
    }

    private Map<String, Object> getDocData(AssistanceEntity assistanceEntity) {
        Map<String, Object> Data = new HashMap<>();
        //Data.put("id", eventEntity.getId());
        Data.put("studentName", assistanceEntity.getStudentName());
        Data.put("eventName", assistanceEntity.getEventName());
        Data.put("attented", assistanceEntity.getAttented());
        return Data;
    }
}
