package com.devsz.studenteventsapi.service;

import com.devsz.studenteventsapi.firebase.FirebaseInitializer;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.ExecutionException;

@Service
@RequiredArgsConstructor
public class EventStatusUpdater {
    
    private final FirebaseInitializer firebaseInitializer;

    // Ejecutar diariamente a las 00:00 (medianoche) hora de Lima
    @Scheduled(cron = "0 0 0 * * ?", zone = "America/Lima")
    public void updateFinishedEvents() {
        try {
            Firestore firestore = firebaseInitializer.getFirestore();
            String now = LocalDateTime.now(ZoneId.of("America/Lima"))
                                .format(DateTimeFormatter.ISO_DATE_TIME);
            
            List<QueryDocumentSnapshot> events = firestore.collection("events")
                .whereNotIn("status", List.of("Finalizado", "Cancelado"))
                .whereLessThan("eventDateTime", now) 
                .get()
                .get()
                .getDocuments();
            
            if (!events.isEmpty()) {
                var batch = firestore.batch();
                events.forEach(doc -> 
                    batch.update(doc.getReference(), "status", "Finalizado")
                );
                batch.commit().get();
                System.out.println("[EventStatusUpdater] Actualizados " + events.size() + " eventos a las " + now);
            }
            
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.println("[EventStatusUpdater] Proceso interrumpido");
        } catch (ExecutionException e) {
            System.err.println("[EventStatusUpdater] Error: " + e.getMessage());
        }
    }
}