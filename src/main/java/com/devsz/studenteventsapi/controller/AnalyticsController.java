package com.devsz.studenteventsapi.controller;

import com.devsz.studenteventsapi.dto.analytics.AnalyticsDTOs;
import com.devsz.studenteventsapi.dto.analytics.CountResponse;
import com.devsz.studenteventsapi.dto.analytics.EventCountResponse;
import com.devsz.studenteventsapi.dto.analytics.ModalityCountResponse;
import com.devsz.studenteventsapi.dto.analytics.MonthCountResponse;
import com.devsz.studenteventsapi.dto.analytics.TypeCountResponse;
import com.devsz.studenteventsapi.exception.AnalyticsException;
import com.devsz.studenteventsapi.service.impl.AnalyticsService;
import org.springframework.http.HttpStatus;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Map;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/api/analytics")
@RequiredArgsConstructor
public class AnalyticsController {
    private final AnalyticsService analyticsService;

    // 1. Resumen de eventos por estado
    @GetMapping("/events/summary")
    public ResponseEntity<Map<String, Long>> getEventSummary(Principal principal) {
        try {
            return ResponseEntity.ok(analyticsService.getEventStatusSummary(principal.getName()));
        } catch (ExecutionException | InterruptedException e) {
            throw new AnalyticsException("Error al obtener resumen de eventos", e);
        }
    }

    // 2. Total de participantes
    @GetMapping("/participants/total") // parcialmente bien
    public ResponseEntity<CountResponse> getTotalParticipants(Principal principal) {
        try {
            return ResponseEntity.ok(new CountResponse(analyticsService.getTotalParticipants(principal.getName())));
        } catch (ExecutionException | InterruptedException e) {
            throw new AnalyticsException("Error al contar participantes", e);
        }
    }

    // 3. Total respuestas a encuestas
    @GetMapping("/survey-responses/total")
    public ResponseEntity<CountResponse> getTotalSurveyResponses(Principal principal) {
        try {
            return ResponseEntity.ok(new CountResponse(analyticsService.getTotalSurveyResponses(principal.getName())));
        } catch (ExecutionException | InterruptedException e) {
            throw new AnalyticsException("Error al contar respuestas de encuestas", e);
        }
    }

    // 4. Eventos por mes
    @GetMapping("/events/by-month")
    public ResponseEntity<AnalyticsDTOs<MonthCountResponse>> getEventsByMonth(Principal principal) {
        try {
            return ResponseEntity.ok(new AnalyticsDTOs<>(analyticsService.getEventsByMonth(principal.getName())));
        } catch (ExecutionException | InterruptedException e) {
            throw new AnalyticsException("Error al obtener eventos por mes", e);
        }
    }

    // 5. Participantes por mes
    @GetMapping("/participants/by-month")
    public ResponseEntity<AnalyticsDTOs<MonthCountResponse>> getParticipantsByMonth(Principal principal) {
        try {
            return ResponseEntity.ok(new AnalyticsDTOs<>(analyticsService.getParticipantsByMonth(principal.getName())));
        } catch (ExecutionException | InterruptedException e) {
            throw new AnalyticsException("Error al obtener participantes por mes", e);
        }
    }

    // 7. Eventos por tipo
    @GetMapping("/events/by-type")
    public ResponseEntity<AnalyticsDTOs<TypeCountResponse>> getEventsByType(Principal principal) {
        try {
            return ResponseEntity.ok(new AnalyticsDTOs<>(analyticsService.getEventsByType(principal.getName())));
        } catch (ExecutionException | InterruptedException e) {
            throw new AnalyticsException("Error al obtener eventos por tipo", e);
        }
    }

    // 9. Eventos por modalidad
    @GetMapping("/events/by-modality")
    public ResponseEntity<AnalyticsDTOs<ModalityCountResponse>> getEventsByModality(Principal principal) {
        try {
            return ResponseEntity.ok(new AnalyticsDTOs<>(analyticsService.getEventsByModality(principal.getName())));
        } catch (ExecutionException | InterruptedException e) {
            throw new AnalyticsException("Error al obtener eventos por modalidad", e);
        }
    }

    // 10. Participantes por evento
    @GetMapping("/participants/per-event")
    public ResponseEntity<AnalyticsDTOs<EventCountResponse>> getParticipantsPerEvent(Principal principal) {
        try {
            return ResponseEntity
                    .ok(new AnalyticsDTOs<>(analyticsService.getParticipantsPerEvent(principal.getName())));
        } catch (ExecutionException | InterruptedException e) {
            throw new AnalyticsException("Error al obtener participantes por evento", e);
        }
    }

    // Manejo de excepciones
    @ExceptionHandler(AnalyticsException.class)
    public ResponseEntity<String> handleAnalyticsException(AnalyticsException ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }
}