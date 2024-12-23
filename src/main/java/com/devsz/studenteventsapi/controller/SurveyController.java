package com.devsz.studenteventsapi.controller;

import com.devsz.studenteventsapi.dto.DataRequest;
import com.devsz.studenteventsapi.dto.PathRequest;
import com.devsz.studenteventsapi.entity.SurveyEntity;
import com.devsz.studenteventsapi.service.ISurveyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/surveys")
@RequiredArgsConstructor
public class SurveyController {

    private final ISurveyService service;

    @PostMapping
    public ResponseEntity<SurveyEntity> create(@RequestBody DataRequest<SurveyEntity> data) throws Exception {
        return new ResponseEntity<>(service.save(data.path(), data.entity()), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<SurveyEntity> update(
            @PathVariable String id,
            @RequestBody DataRequest<SurveyEntity> data) throws Exception {
        return new ResponseEntity<>(service.update(data.path(), id, data.entity()), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<SurveyEntity>> readAll(
            @RequestBody PathRequest pathRequest) throws Exception {
        return new ResponseEntity<>(service.readAll(pathRequest), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SurveyEntity> readById(
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
