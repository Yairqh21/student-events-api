package com.devsz.studenteventsapi.controller;

import com.devsz.studenteventsapi.Utils.FirebaseUtils;
import com.devsz.studenteventsapi.dto.DataRequest;
import com.devsz.studenteventsapi.dto.PathRequest;
import com.devsz.studenteventsapi.entity.SurveyEntity;
import com.devsz.studenteventsapi.service.ISurveyService;

import jakarta.validation.constraints.NotEmpty;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/surveys")
@RequiredArgsConstructor
public class SurveyController {
    private final ISurveyService service;

    @PostMapping
    public ResponseEntity<SurveyEntity> create(
            @RequestBody DataRequest<SurveyEntity> data,
            Principal principal) throws Exception {
        if (FirebaseUtils.checkOwnership(data.entity().getUserId(), principal.getName())) {
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(service.save(data.path(), data.entity()));
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<SurveyEntity> update(
            @PathVariable String id,
            @RequestBody DataRequest<SurveyEntity> data,
            Principal principal) throws Exception {
        if (FirebaseUtils.checkOwnership(data.entity().getUserId(), principal.getName())) {
            return ResponseEntity.ok(service.update(data.path(), id, data.entity()));
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    @GetMapping
    public ResponseEntity<List<SurveyEntity>> readAll(
            @RequestParam @NotEmpty List<String> pathSegments, Principal principal) throws Exception {
        PathRequest pathRequest = new PathRequest(pathSegments.toArray(new String[0]));
        String userId = principal.getName();
        List<SurveyEntity> surveys = service.readAll(pathRequest).stream()
                .filter(survey -> survey.getUserId().equals(userId)).toList();
        return ResponseEntity.ok(surveys);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SurveyEntity> readById(
            @PathVariable String id,
            @RequestParam @NotEmpty List<String> pathSegments) throws Exception {
        PathRequest pathRequest = new PathRequest(pathSegments.toArray(new String[0]));
        return ResponseEntity.ok(service.readById(pathRequest, id));
    }

    @GetMapping("/event/{eventId}")
    public ResponseEntity<SurveyEntity> readByEventId(
            @PathVariable String eventId,
            @RequestParam @NotEmpty List<String> pathSegments) throws Exception {
        PathRequest pathRequest = new PathRequest(pathSegments.toArray(new String[0]));
        return ResponseEntity.ok(service.readByEventId(pathRequest, eventId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable String id,
            @RequestParam @NotEmpty List<String> pathSegments) throws Exception {
        PathRequest pathRequest = new PathRequest(pathSegments.toArray(new String[0]));
        service.delete(pathRequest, id);
        return ResponseEntity.ok().build();
    }
}