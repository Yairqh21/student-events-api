package com.devsz.studenteventsapi.controller;

import com.devsz.studenteventsapi.Utils.FirebaseUtils;
import com.devsz.studenteventsapi.dto.DataRequest;
import com.devsz.studenteventsapi.dto.PathRequest;
import com.devsz.studenteventsapi.entity.EventEntity;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import com.devsz.studenteventsapi.service.IEventService;

import java.security.Principal;
import java.sql.Timestamp;
import java.util.List;

@RestController
@RequestMapping("/api/events")
@RequiredArgsConstructor
// @PreAuthorize("hasRole('ADMIN')")
public class EventController {

    private final IEventService service;

    @PostMapping
    public ResponseEntity<EventEntity> create(@RequestBody DataRequest<EventEntity> data, Principal principal)
            throws Exception {
        if (FirebaseUtils.checkOwnership(data.entity().getUserCreatedId(), principal.getName())) {
            return new ResponseEntity<>(service.save(data.path(), data.entity()), HttpStatus.CREATED);
        }
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);

    }

    @PutMapping("/{id}")
    public ResponseEntity<EventEntity> update(
            @PathVariable String id,
            @RequestBody DataRequest<EventEntity> data, Principal principal) throws Exception {
        if (FirebaseUtils.checkOwnership(data.entity().getUserCreatedId(), principal.getName())) {
            return new ResponseEntity<>(service.update(data.path(), id, data.entity()), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

    @GetMapping
    // @PreAuthorize("hasRole('ADMIN') or hasRole('STUDENT') or hasRole('DEV')")
    public ResponseEntity<List<EventEntity>> readAll1(
            @RequestParam("pathSegments") List<String> pathSegments) throws Exception {
        PathRequest pathRequest = new PathRequest(pathSegments.toArray(new String[0]));
        return new ResponseEntity<>(service.readAll(pathRequest), HttpStatus.OK);
    }

    @GetMapping("/paginated")
    public ResponseEntity<List<EventEntity>> readAll(
            @RequestParam("pathSegments") List<String> pathSegments,
            @RequestParam(value = "limit", defaultValue = "10") int limit,
            @RequestParam(value = "startAfter", required = false) Long startAfterTimestamp // epoch millis
    ) throws Exception {

        PathRequest pathRequest = new PathRequest(pathSegments.toArray(new String[0]));
        Timestamp startAfter = (startAfterTimestamp != null)
                ? new Timestamp(startAfterTimestamp)
                : null;

        List<EventEntity> events = service.readAllPaginated(pathRequest, limit, startAfter).stream()
                .filter(event -> event.getStatus().equalsIgnoreCase("Pendiente")
                        || event.getStatus().equalsIgnoreCase("En Progreso"))
                .toList();
        return new ResponseEntity<>(events, HttpStatus.OK);
    }

    @GetMapping("/created-by")
    public ResponseEntity<List<EventEntity>> getEventsCreatedByUser(
            Principal principal,
            @RequestParam("pathSegments") List<String> pathSegments,
            @RequestParam(defaultValue = "userCreatedId") String field,
            @RequestParam(defaultValue = "10") int limit,
            @RequestParam(value = "startAfter", required = false) Long startAfterMillis) throws Exception {
        Timestamp startAfter = (startAfterMillis != null) ? new Timestamp(startAfterMillis) : null;
        PathRequest pathRequest = new PathRequest(pathSegments.toArray(new String[0]));

        String userId = principal.getName(); 
        List<EventEntity> events = service.readAllOfUserCreated(pathRequest, userId, field, limit, startAfter);
        return ResponseEntity.ok(events);

    }

    @GetMapping("/{id}")
    public ResponseEntity<EventEntity> readById(
            @PathVariable String id,
            @RequestParam("pathSegments") List<String> pathSegments) throws Exception {
        PathRequest pathRequest = new PathRequest(pathSegments.toArray(new String[0]));
        return new ResponseEntity<>(service.readById(pathRequest, id), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable String id,
            @RequestParam("pathSegments") List<String> pathSegments) throws Exception {
        PathRequest pathRequest = new PathRequest(pathSegments.toArray(new String[0]));
        service.delete(pathRequest, id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
