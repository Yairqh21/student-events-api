package com.devsz.studenteventsapi.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.devsz.studenteventsapi.dto.DataRequest;
import com.devsz.studenteventsapi.dto.PathRequest;
import com.devsz.studenteventsapi.entity.EventParticipationEntity;
import com.devsz.studenteventsapi.service.IEventParticipationService;

import java.util.List;

@RestController
@RequestMapping("/api/event-participation")
@RequiredArgsConstructor
public class EventParticipationController {

    private final IEventParticipationService service;

    @PostMapping
    public ResponseEntity<EventParticipationEntity> create(@RequestBody DataRequest<EventParticipationEntity> data)
            throws Exception {
        return new ResponseEntity<>(service.save(data.path(), data.entity()), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EventParticipationEntity> update(
            @PathVariable String id,
            @RequestBody DataRequest<EventParticipationEntity> data) throws Exception {
        return new ResponseEntity<>(service.update(data.path(), id, data.entity()), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<EventParticipationEntity>> readAll(
            @RequestBody PathRequest pathRequest) throws Exception {
        return new ResponseEntity<>(service.readAll(pathRequest), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EventParticipationEntity> readById(
            @PathVariable String id,
            @RequestBody PathRequest pathRequest) throws Exception {
        return new ResponseEntity<>(service.readById(pathRequest, id), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable String id,
            @RequestBody PathRequest pathRequest) throws Exception {
        service.delete(pathRequest, id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
