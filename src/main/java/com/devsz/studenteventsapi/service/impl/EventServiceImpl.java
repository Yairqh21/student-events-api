package com.devsz.studenteventsapi.service.impl;

import com.devsz.studenteventsapi.entity.EventEntity;
import com.devsz.studenteventsapi.firebase.FirebaseInitializer;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.cloud.firestore.WriteResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.devsz.studenteventsapi.service.IEventService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

@Service
@RequiredArgsConstructor
public class EventServiceImpl implements IEventService {


    // @Autowired
    private final FirebaseInitializer firebase;

    @Override
    public EventEntity save(EventEntity eventEntity) throws Exception {
        Map<String, Object> Data = getDocData(eventEntity);

        ApiFuture<WriteResult> writeResultApiFuture = getCollection().document().create(Data);

        try {
            if (null != writeResultApiFuture.get()) {
                return eventEntity;
            }
        } catch (Exception e) {
            return null;
        }
        return null;
    }


    @Override
    public EventEntity update(EventEntity eventEntity, String id) throws Exception {
        Map<String, Object> Data = getDocData(eventEntity);
        ApiFuture<WriteResult> writeResultApiFuture = getCollection().document(id).set(Data);
        try {
            if (null != writeResultApiFuture.get()) {
                return eventEntity;
            }
        } catch (Exception e) {
            return null;
        }
        return null;
    }

    @Override
    public List<EventEntity> readAll() throws Exception {
        List<EventEntity> response = new ArrayList<>();
        EventEntity eventEntity;

        ApiFuture<QuerySnapshot> querySnapshotApiFuture = getCollection().get();
        try {
            for (DocumentSnapshot doc : querySnapshotApiFuture.get().getDocuments()) {
                eventEntity = doc.toObject(EventEntity.class);
                eventEntity.setId(doc.getId());
                response.add(eventEntity);

            }
            return response;
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public EventEntity readById(String id) throws Exception {
        ApiFuture<DocumentSnapshot> documentSnapshotApiFuture = getCollection().document(id).get();
        try {
            DocumentSnapshot documentSnapshot = documentSnapshotApiFuture.get();
            if (documentSnapshot.exists()) {
                EventEntity eventEntity = documentSnapshot.toObject(EventEntity.class);
                eventEntity.setId(documentSnapshot.getId());
                return eventEntity;
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
        return firebase.getFirestore().collection("events");
    }

    private Map<String, Object> getDocData(EventEntity eventEntity) {
        Map<String, Object> Data = new HashMap<>();
        //Data.put("id", eventEntity.getId());
        Data.put("eventDateTime", eventEntity.getEventDateTime());
        Data.put("eventName", eventEntity.getEventName());
        Data.put("eventType", eventEntity.getEventType());
        Data.put("modality", eventEntity.getModality());
        Data.put("ubication", eventEntity.getUbication());
        Data.put("forStudentsOf", eventEntity.getForStudentsOf());
        Data.put("urlEventPhoto", eventEntity.getUrlEventPhoto());
        return Data;
    }
}
