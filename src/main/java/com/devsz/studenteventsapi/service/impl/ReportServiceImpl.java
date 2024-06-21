package com.devsz.studenteventsapi.service.impl;

import com.devsz.studenteventsapi.entity.ReportEntity;
import com.devsz.studenteventsapi.firebase.FirebaseInitializer;
import com.devsz.studenteventsapi.service.IReportService;
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
public class ReportServiceImpl implements IReportService {


    // @Autowired
    private final FirebaseInitializer firebase;

    @Override
    public ReportEntity save(ReportEntity reportEntity) throws Exception {
        Map<String, Object> Data = getDocData(reportEntity);

        ApiFuture<WriteResult> writeResultApiFuture = getCollection().document().create(Data);

        try {
            if (null != writeResultApiFuture.get()) {
                return reportEntity;
            }
        } catch (Exception e) {
            return null;
        }
        return null;
    }


    @Override
    public ReportEntity update(ReportEntity reportEntity, String id) throws Exception {
        Map<String, Object> Data = getDocData(reportEntity);
        ApiFuture<WriteResult> writeResultApiFuture = getCollection().document(id).set(Data);
        try {
            if (null != writeResultApiFuture.get()) {
                return reportEntity;
            }
        } catch (Exception e) {
            return null;
        }
        return null;
    }

    @Override
    public List<ReportEntity> readAll() throws Exception {
        List<ReportEntity> response = new ArrayList<>();
        ReportEntity reportEntity;

        ApiFuture<QuerySnapshot> querySnapshotApiFuture = getCollection().get();
        try {
            for (DocumentSnapshot doc : querySnapshotApiFuture.get().getDocuments()) {
                reportEntity = doc.toObject(ReportEntity.class);
                reportEntity.setId(doc.getId());
                response.add(reportEntity);

            }
            return response;
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public ReportEntity readById(String id) throws Exception {
        ApiFuture<DocumentSnapshot> documentSnapshotApiFuture = getCollection().document(id).get();
        try {
            DocumentSnapshot documentSnapshot = documentSnapshotApiFuture.get();
            if (documentSnapshot.exists()) {
                ReportEntity reportEntity = documentSnapshot.toObject(ReportEntity.class);
                reportEntity.setId(documentSnapshot.getId());
                return reportEntity;
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
        return firebase.getFirestore().collection("reports");
    }

    private Map<String, Object> getDocData(ReportEntity reportEntity) {
        Map<String, Object> Data = new HashMap<>();
        //Data.put("id", eventEntity.getId());
        Data.put("reportName", reportEntity.getReportName());
        Data.put("surveyName", reportEntity.getSurveyName());
        Data.put("assistanceAmount", reportEntity.getAssistanceAmount());
        Data.put("attendanceAmountEvent", reportEntity.getAttendanceAmountEvent());
        Data.put("assistanceAmountModality", reportEntity.getAssistanceAmountModality());
        Data.put("attendaceAmountRace", reportEntity.getAttendaceAmountRace());
        Data.put("amountAssistanceCycle", reportEntity.getAmountAssistanceCycle());
        return Data;
    }
}
