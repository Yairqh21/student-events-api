package com.devsz.studenteventsapi.service.impl;

import com.devsz.studenteventsapi.Utils.FirestoreUtils;
import com.devsz.studenteventsapi.dto.PathRequest;
import com.devsz.studenteventsapi.entity.BaseEntity;
import com.devsz.studenteventsapi.firebase.FirebaseInitializer;
import com.devsz.studenteventsapi.service.ICRUD;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import lombok.RequiredArgsConstructor;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

@RequiredArgsConstructor
public abstract class BaseServiceImpl<T, ID> implements ICRUD<T, ID> {

    private final FirebaseInitializer fireInit;

    protected abstract Class<T> getEntityClass();

    @Override
    public void delete(PathRequest pathRequest, ID id) throws ExecutionException, InterruptedException {
        FirestoreUtils
                .getCollectionReference(fireInit, pathRequest.pathSegments()).document(id.toString()).delete().get();
    }

    @Override
    public List<T> readAll(PathRequest pathRequest) throws ExecutionException, InterruptedException {
        List<T> result = new ArrayList<>();
        ApiFuture<QuerySnapshot> query = FirestoreUtils
                .getCollectionReference(fireInit, pathRequest.pathSegments()).get();
        List<QueryDocumentSnapshot> documents = query.get().getDocuments();
        for (QueryDocumentSnapshot doc : documents) {
            T object = doc.toObject(getEntityClass());
            result.add(object);
        }
        return result;
    }

    @Override
    public T readById(PathRequest pathRequest, ID id) throws ExecutionException, InterruptedException {
        DocumentReference ref = FirestoreUtils
                .getCollectionReference(fireInit, pathRequest.pathSegments()).document(id.toString());
        ApiFuture<DocumentSnapshot> futureDoc = ref.get();
        DocumentSnapshot document = futureDoc.get();
        if (document.exists()) {
            T object = document.toObject(getEntityClass());
            return object;
        }
        return null;
    }

    @Override
    public T save(PathRequest pathRequest, T data) throws ExecutionException, InterruptedException {
        ApiFuture<DocumentReference> documentReferenceApiFuture = FirestoreUtils
                .getCollectionReference(fireInit, pathRequest.pathSegments())
                .add(data);

        DocumentReference documentReference = documentReferenceApiFuture.get();
        if (documentReference != null) {
            if (data instanceof BaseEntity) {
                ((BaseEntity) data).setId(documentReference.getId());
            }
            return data;
        }
        return null;
    }

    @Override
    public T update(PathRequest pathRequest, ID id, T data)
            throws ExecutionException, InterruptedException, IllegalAccessException {
        Map<String, Object> updates = filterNullFields(data);

        if (!updates.isEmpty()) {
            ApiFuture<WriteResult> writeResultApiFuture = FirestoreUtils
                    .getCollectionReference(fireInit, pathRequest.pathSegments())
                    .document(id.toString()).update(updates);

            if (null != writeResultApiFuture.get()) {
                if (data instanceof BaseEntity) {
                    ((BaseEntity) data).setId(id.toString());
                }
                return data;
            }
        }

        return null;
    }

    private Map<String, Object> filterNullFields(Object data) throws IllegalAccessException {
        Map<String, Object> updates = new HashMap<>();

        for (Field field : data.getClass().getDeclaredFields()) {
            field.setAccessible(true);
            Object value = field.get(data);

            if (value != null) {
                updates.put(field.getName(), value);
            }
        }

        if (data instanceof BaseEntity) {
            BaseEntity entity = (BaseEntity) data;
            entity.onUpdate();
            updates.put("updatedAt", entity.getUpdatedAt());
        }

        return updates;
    }

}
