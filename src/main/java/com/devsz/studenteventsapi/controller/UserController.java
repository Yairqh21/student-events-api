package com.devsz.studenteventsapi.controller;

import com.devsz.studenteventsapi.entity.UserEntity;
import com.devsz.studenteventsapi.service.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    //@Autowired
    private final IUserService service;

    @GetMapping
    public ResponseEntity<List<UserEntity>> readAll() throws Exception {
        return new ResponseEntity<>(service.readAll(), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<UserEntity> create(@RequestBody UserEntity userEntity) throws Exception {
        return new ResponseEntity<>(service.save(userEntity), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserEntity> update(@PathVariable(value = "id") String id, @RequestBody UserEntity userEntity) throws Exception {
        return new ResponseEntity<>(service.update(userEntity, id), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable(value = "id") String id) throws Exception {
        service.delete(id);
        return new ResponseEntity<>(null, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserEntity> readById(@PathVariable(value = "id") String id) throws Exception {
        return new ResponseEntity<>(service.readById(id), HttpStatus.OK);
    }

}
