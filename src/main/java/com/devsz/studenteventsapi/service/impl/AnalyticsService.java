package com.devsz.studenteventsapi.service.impl;

import lombok.RequiredArgsConstructor;

import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.devsz.studenteventsapi.dto.PathRequest;
import com.devsz.studenteventsapi.firebase.FirebaseInitializer;
import com.devsz.studenteventsapi.service.IEventService;
import com.devsz.studenteventsapi.service.ISurveyService;
import com.google.api.core.ApiFuture;
import com.devsz.studenteventsapi.entity.*;
import com.google.cloud.firestore.*;
import com.devsz.studenteventsapi.dto.analytics.*;
import com.google.cloud.firestore.Firestore;

import java.time.ZoneId;
import java.util.*;

@Service
@RequiredArgsConstructor
public class AnalyticsService {
    private final IEventService eventService;
    private final ISurveyService surveyService;
    private final FirebaseInitializer firebaseInitializer;

    // 1. Resumen de eventos por estado
    public Map<String, Long> getEventStatusSummary(String organizerId) throws ExecutionException, InterruptedException {
        List<EventEntity> events = eventService.readAll(new PathRequest(new String[]{"events"}));
        return events.stream()
                .filter(event -> organizerId.equals(event.getUserCreatedId()))
                .collect(Collectors.groupingBy(
                    EventEntity::getStatus,
                    Collectors.counting()
                ));
    }

    // 2. Total de participantes
    public long getTotalParticipants(String organizerId) throws ExecutionException, InterruptedException {
        Set<String> eventIds = getOrganizerEventIds(organizerId);
        if (eventIds.isEmpty()) return 0L;

        Firestore db = firebaseInitializer.getFirestore();
        ApiFuture<QuerySnapshot> query = db.collectionGroup("eventParticipation").get();

        // System.out.println(query.get().getDocuments());
        return query.get().getDocuments().stream()
                .filter(doc -> eventIds.contains(doc.getString("eventId")))
                .count();
    }

    // 3. Total respuestas a encuestas
    public long getTotalSurveyResponses(String organizerId) throws ExecutionException, InterruptedException {
        List<SurveyEntity> surveys = surveyService.readAll(new PathRequest(new String[]{"surveys"}));
        Set<String> surveyIds = surveys.stream()
                .filter(survey -> organizerId.equals(survey.getUserId()))
                .map(SurveyEntity::getId)
                .map(String::valueOf)
                .collect(Collectors.toSet());

        if (surveyIds.isEmpty()) return 0L;

        Firestore db = firebaseInitializer.getFirestore();
        ApiFuture<QuerySnapshot> query = db.collection("responses").get();
        return query.get().getDocuments().stream()
                .filter(doc -> surveyIds.contains(doc.getString("surveyId")))
                .count();
    }

    // 4. Eventos por mes
    public List<MonthCountResponse> getEventsByMonth(String organizerId) throws ExecutionException, InterruptedException {
        List<EventEntity> events = eventService.readAll(new PathRequest(new String[]{"events"}));
        
        Map<String, Long> monthlyCount = events.stream()
                .filter(event -> organizerId.equals(event.getUserCreatedId()))
                .collect(Collectors.groupingBy(
                event -> (String) event.getEventDateTime().toString().toLowerCase(),
                Collectors.counting()
                ));

        return monthlyCount.entrySet().stream()
                .map(entry -> new MonthCountResponse(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());
    }

    // 5. Participantes por mes
    public List<MonthCountResponse> getParticipantsByMonth(String organizerId) throws ExecutionException, InterruptedException {
        Set<String> eventIds = getOrganizerEventIds(organizerId);
        if (eventIds.isEmpty()) return Collections.emptyList();

        Firestore db = firebaseInitializer.getFirestore();
        ApiFuture<QuerySnapshot> query = db.collectionGroup("eventParticipation").get();
        List<QueryDocumentSnapshot> docs = query.get().getDocuments();

        Map<String, Long> monthlyCount = docs.stream()
                .filter(doc -> eventIds.contains(doc.getString("eventId")))
                .collect(Collectors.groupingBy(
                    doc -> doc.getCreateTime().toDate().toInstant()
                            .atZone(ZoneId.systemDefault())
                            .getMonth().toString().toLowerCase(),
                    Collectors.counting()
                ));

        return monthlyCount.entrySet().stream()
                .map(entry -> new MonthCountResponse(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());
    }

    // 6. Usuarios por carrera que participaron al evento del creador

    // 7. Eventos por tipo
    public List<TypeCountResponse> getEventsByType(String organizerId) throws ExecutionException, InterruptedException {
        List<EventEntity> events = eventService.readAll(new PathRequest(new String[]{"events"}));
        
        Map<String, Long> typeCount = events.stream()
                .filter(event -> organizerId.equals(event.getUserCreatedId()))
                .collect(Collectors.groupingBy(
                    EventEntity::getEventType,
                    Collectors.counting()
                ));

        return typeCount.entrySet().stream()
                .map(entry -> new TypeCountResponse(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());
    }

    // 8. Estudiantes por ciclo que participaron al evento del creador
    // 9. Eventos por modalidad
    public List<ModalityCountResponse> getEventsByModality(String organizerId) throws ExecutionException, InterruptedException {
        List<EventEntity> events = eventService.readAll(new PathRequest(new String[]{"events"}));
        
        Map<String, Long> modalityCount = events.stream()
                .filter(event -> organizerId.equals(event.getUserCreatedId()))
                .collect(Collectors.groupingBy(
                    EventEntity::getEventModality,
                    Collectors.counting()
                ));

        return modalityCount.entrySet().stream()
                .map(entry -> new ModalityCountResponse(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());
    }

    // 10. Participantes por evento
    public List<EventCountResponse> getParticipantsPerEvent(String organizerId) throws ExecutionException, InterruptedException {
        List<EventEntity> events = eventService.readAll(new PathRequest(new String[]{"events"}));
        
        List<EventEntity> organizerEvents = events.stream()
                .filter(event -> organizerId.equals(event.getUserCreatedId()))
                .collect(Collectors.toList());

        Set<String> eventIds = organizerEvents.stream()
                .map(EventEntity::getId)
                .map(String::valueOf)
                .collect(Collectors.toSet());

        if (eventIds.isEmpty()) return Collections.emptyList();

        Firestore db = firebaseInitializer.getFirestore();
        ApiFuture<QuerySnapshot> query = db.collectionGroup("eventParticipation").get();
        List<QueryDocumentSnapshot> docs = query.get().getDocuments();

        Map<String, Long> participationCount = docs.stream()
                .filter(doc -> eventIds.contains(doc.getString("eventId")))
                .collect(Collectors.groupingBy(
                    doc -> doc.getString("eventId"),
                    Collectors.counting()
                ));

        return organizerEvents.stream()
                .map(event -> new EventCountResponse(
                    event.getEventName(),
                    participationCount.getOrDefault(String.valueOf(event.getId()), 0L)
                ))
                .collect(Collectors.toList());
    }

    // Método auxiliar para obtener IDs de eventos del organizador
    private Set<String> getOrganizerEventIds(String organizerId) throws ExecutionException, InterruptedException {
        List<EventEntity> events = eventService.readAll(new PathRequest(new String[]{"events"}));
        return events.stream()
                .filter(event -> organizerId.equals(event.getUserCreatedId()))
                .map(EventEntity::getId)
                .map(String::valueOf)
                .collect(Collectors.toSet());
    }
}
