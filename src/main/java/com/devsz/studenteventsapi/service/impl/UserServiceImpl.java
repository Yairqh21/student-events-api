package com.devsz.studenteventsapi.service.impl;

import com.devsz.studenteventsapi.entity.UserEntity;
import com.devsz.studenteventsapi.firebase.FirebaseInitializer;
import com.devsz.studenteventsapi.service.IUserService;
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
public class UserServiceImpl implements IUserService {


    // @Autowired
    private final FirebaseInitializer firebase;

    @Override
    public UserEntity save(UserEntity userEntity) throws Exception {
        Map<String, Object> Data = getDocData(userEntity);

        ApiFuture<WriteResult> writeResultApiFuture = getCollection().document().create(Data);

        try {
            if (null != writeResultApiFuture.get()) {
                return userEntity;
            }
        } catch (Exception e) {
            return null;
        }
        return null;
    }


    @Override
public UserEntity update(UserEntity userEntity, String id) throws Exception {
    Map<String, Object> data = getDocData(userEntity);
    ApiFuture<WriteResult> writeResultApiFuture = getCollection().document(id).set(data);
    try {
        // Esperar a que se complete la operación de escritura
        WriteResult writeResult = writeResultApiFuture.get();
        if (writeResult != null && writeResult.getUpdateTime() != null) {
            return userEntity;
        } else {
            throw new Exception("La operación de escritura no se completó correctamente");
        }
    } catch (Exception e) {
        
        e.printStackTrace();
        throw e;
    }
}


    @Override
    public List<UserEntity> readAll() throws Exception {
        List<UserEntity> response = new ArrayList<>();
        UserEntity userEntity;

        ApiFuture<QuerySnapshot> querySnapshotApiFuture = getCollection().get();
        try {
            for (DocumentSnapshot doc : querySnapshotApiFuture.get().getDocuments()) {
                userEntity = doc.toObject(UserEntity.class);
                userEntity.setId(doc.getId());
                response.add(userEntity);

            }
            return response;
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public UserEntity readById(String id) throws Exception {
        ApiFuture<DocumentSnapshot> documentSnapshotApiFuture = getCollection().document(id).get();
        try {
            DocumentSnapshot documentSnapshot = documentSnapshotApiFuture.get();
            if (documentSnapshot.exists()) {
                UserEntity userEntity = documentSnapshot.toObject(UserEntity.class);
                userEntity.setId(documentSnapshot.getId());
                return userEntity;
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
        return firebase.getFirestore().collection("users");
    }

    private Map<String, Object> getDocData(UserEntity userEntity) {
        Map<String, Object> Data = new HashMap<>();
        Data.put("id", userEntity.getId());
        Data.put("username", userEntity.getUsername());
        Data.put("password", userEntity.getPassword());
        Data.put("email", userEntity.getEmail());
        //otros datos
        Data.put("fullName",userEntity.getFullName());
        Data.put("numberPhone",userEntity.getNumberPhone());
        Data.put("career",userEntity.getCareer());
        Data.put("academicCycle",userEntity.getAcademicCycle());
        Data.put("userImg",userEntity.getUserImg());
        return Data;
    }
}
