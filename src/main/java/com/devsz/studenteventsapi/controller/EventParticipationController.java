package com.devsz.studenteventsapi.controller;

import com.devsz.studenteventsapi.dto.PathRequest;
import com.devsz.studenteventsapi.dto.UserParticipationEvent;
import com.devsz.studenteventsapi.entity.EventEntity;
import com.devsz.studenteventsapi.entity.EventParticipationEntity;
import com.devsz.studenteventsapi.service.IEventParticipationService;
import com.devsz.studenteventsapi.service.IEventService;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.security.Principal;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/api/event-participation")
@RequiredArgsConstructor
// @PreAuthorize("hasRole('STUDENT')")
public class EventParticipationController {

    private static final String PARTICIPATION_PATH = "eventParticipation";

    private final IEventParticipationService participationService;
    private final IEventService eventService;

    @PostMapping
    public ResponseEntity<EventParticipationEntity> createParticipation(
            @RequestBody EventParticipationEntity participation,
            Principal principal) throws Exception {

        validateAndSetParticipant(participation, principal);
        PathRequest path = buildPathRequest(principal);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(participationService.save(path, participation));
    }

    @PutMapping("/{id}")
    public ResponseEntity<EventParticipationEntity> updateParticipation(
            @PathVariable String id,
            @RequestBody EventParticipationEntity participation,
            Principal principal) throws Exception {

        validateAndSetParticipant(participation, principal);
        PathRequest path = buildPathRequest(principal);

        return ResponseEntity.ok(participationService.update(path, id, participation));
    }

    @GetMapping
    public ResponseEntity<List<UserParticipationEvent>> getAllParticipations(Principal principal) throws Exception {
        PathRequest path = buildPathRequest(principal);
        List<EventEntity> events = eventService.readAll(new PathRequest(new String[] { "events" })).stream()
                .filter(event -> event.getStatus().equalsIgnoreCase("Pendiente")
                        || event.getStatus().equalsIgnoreCase("En Progreso"))
                .toList();
        List<EventParticipationEntity> participations = participationService.readAll(path);

        // Filtrar participaciones que no tienen evento correspondiente
        List<UserParticipationEvent> participationEvents = participations.stream()
                .map(participation -> {
                    EventEntity event = events.stream()
                            .filter(e -> e.getId().equals(participation.getEventId()))
                            .findFirst()
                            .orElse(null);

                    if (event == null)
                        return null;

                    return new UserParticipationEvent(
                            participation.getId(),
                            event.getId(),
                            event.getEventDateTime().toString(),
                            event.getEventName(),
                            event.getLocation(),
                            event.getEventModality(),
                            event.getImgUrl(),
                            participation.getParticipantId(),
                            participation.getIsAttended(),
                            participation.getTicketCode(),
                            participation.getRegistrationDate().toString());
                })
                .filter(Objects::nonNull)
                .toList();

        return ResponseEntity.ok(participationEvents);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EventParticipationEntity> getParticipationById(
            @PathVariable String id,
            Principal principal) throws Exception {

        PathRequest path = buildPathRequest(principal);
        return ResponseEntity.ok(participationService.readById(path, id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteParticipation(
            @PathVariable String id,
            Principal principal) throws Exception {

        PathRequest path = buildPathRequest(principal);
        participationService.delete(path, id);
        return ResponseEntity.noContent().build();
    }


    private void validateAndSetParticipant(EventParticipationEntity participation, Principal principal) {
        String userId = principal.getName();

        if (participation.getParticipantId() != null && !participation.getParticipantId().equals(userId)) {
            throw new SecurityException("El ID del participante no coincide con el usuario autenticado");
        }

        participation.setParticipantId(userId);
    }

    private PathRequest buildPathRequest(Principal principal) {
        String userId = principal.getName();
        return new PathRequest(new String[] { "users", userId, PARTICIPATION_PATH });
    }
}