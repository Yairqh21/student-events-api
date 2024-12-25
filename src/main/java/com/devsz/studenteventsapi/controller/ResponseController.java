package com.devsz.studenteventsapi.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.devsz.studenteventsapi.Utils.FirebaseUtils;
import com.devsz.studenteventsapi.dto.DataRequest;
import com.devsz.studenteventsapi.dto.PathRequest;
import com.devsz.studenteventsapi.entity.ResponsesEntity;
import com.devsz.studenteventsapi.service.IResponseService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/response")
@RequiredArgsConstructor
public class ResponseController {
    private final IResponseService service;

    @PostMapping
    public ResponseEntity<ResponsesEntity> create(@Valid @RequestBody DataRequest<ResponsesEntity> data,
            Principal principal)
            throws Exception {
        if (FirebaseUtils.checkOwnership(data.entity().getUserId(), principal.getName())) {
            return new ResponseEntity<>(service.save(data.path(), data.entity()), HttpStatus.CREATED);
        }
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);

    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponsesEntity> update(
            @PathVariable String id,
            @Valid @RequestBody DataRequest<ResponsesEntity> data, Principal principal) throws Exception {
        if (FirebaseUtils.checkOwnership(data.entity().getUserId(), principal.getName())) {
            return new ResponseEntity<>(service.update(data.path(), id, data.entity()), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);

    }

    @GetMapping
    public ResponseEntity<List<ResponsesEntity>> readAll(
            @Valid @RequestBody PathRequest pathRequest) throws Exception {
        return new ResponseEntity<>(service.readAll(pathRequest), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponsesEntity> readById(
            @PathVariable String id,
            @Valid @RequestBody PathRequest pathRequest) throws Exception {
        return new ResponseEntity<>(service.readById(pathRequest, id), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable String id,
            @Valid @RequestBody PathRequest pathRequest) throws Exception {
        service.delete(pathRequest, id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
