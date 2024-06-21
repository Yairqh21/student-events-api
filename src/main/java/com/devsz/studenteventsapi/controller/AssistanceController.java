package com.devsz.studenteventsapi.controller;

import com.devsz.studenteventsapi.entity.AssistanceEntity;
import com.devsz.studenteventsapi.service.IAssistanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/assistances")
@RequiredArgsConstructor
public class AssistanceController {

    //@Autowired
    private final IAssistanceService service;

    @GetMapping
    public ResponseEntity<List<AssistanceEntity>> readAll() throws Exception {
        return new ResponseEntity<>(service.readAll(), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<AssistanceEntity> create(@RequestBody AssistanceEntity assistanceEntity) throws Exception {
        return new ResponseEntity<>(service.save(assistanceEntity), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AssistanceEntity> update(@PathVariable(value = "id") String id, @RequestBody AssistanceEntity assistanceEntity) throws Exception {
        return new ResponseEntity<>(service.update(assistanceEntity, id), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable(value = "id") String id) throws Exception{
        service.delete(id);
        return new ResponseEntity<>(null, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AssistanceEntity> readById(@PathVariable(value = "id") String id) throws Exception {
        return new ResponseEntity<>(service.readById(id), HttpStatus.OK);
    }

}
