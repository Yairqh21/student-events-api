package com.devsz.studenteventsapi.controller;

import com.devsz.studenteventsapi.dto.DataRequest;
import com.devsz.studenteventsapi.dto.PathRequest;
import com.devsz.studenteventsapi.entity.EventEntity;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.devsz.studenteventsapi.service.IEventService;

import jakarta.validation.Valid;

import java.util.List;

@RestController
@RequestMapping("/api/events")
@RequiredArgsConstructor
public class EventController {

    private final IEventService service;

    @PostMapping
    public ResponseEntity<EventEntity> create(@Valid @RequestBody DataRequest<EventEntity> data) throws Exception {
        return new ResponseEntity<>(service.save(data.path(), data.entity()), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EventEntity> update(
            @PathVariable String id,
            @Valid
            @RequestBody DataRequest<EventEntity> data) throws Exception {
        return new ResponseEntity<>(service.update(data.path(), id, data.entity()), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<EventEntity>> readAll(
        @Valid @RequestBody PathRequest pathRequest) throws Exception {
        return new ResponseEntity<>(service.readAll(pathRequest), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EventEntity> readById(
            @PathVariable String id,
            @Valid
            @RequestBody PathRequest pathRequest) throws Exception {
        return new ResponseEntity<>(service.readById(pathRequest, id), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable String id,
            @Valid
            @RequestBody PathRequest pathRequest) throws Exception {
        service.delete(pathRequest, id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
