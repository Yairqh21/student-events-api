package com.devsz.studenteventsapi.controller;

import com.devsz.studenteventsapi.entity.EventEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.devsz.studenteventsapi.service.IEventService;

import java.util.List;

@RestController
@RequestMapping("/events")
@RequiredArgsConstructor
public class EventController {

    //@Autowired
    private final IEventService service;

    @GetMapping
    public ResponseEntity<List<EventEntity>> readAll() throws Exception {
        return new ResponseEntity<>(service.readAll(), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<EventEntity> create(@RequestBody EventEntity eventEntity) throws Exception {
        return new ResponseEntity<>(service.save(eventEntity), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EventEntity> update(@PathVariable(value = "id") String id, @RequestBody EventEntity eventEntity) throws Exception {
        return new ResponseEntity<>(service.update(eventEntity, id), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable(value = "id") String id) throws Exception{
        service.delete(id);
        return new ResponseEntity<>(null, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EventEntity> readById(@PathVariable(value = "id") String id) throws Exception {
        return new ResponseEntity<>(service.readById(id), HttpStatus.OK);
    }

}
