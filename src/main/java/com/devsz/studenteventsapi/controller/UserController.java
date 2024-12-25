package com.devsz.studenteventsapi.controller;

import com.devsz.studenteventsapi.Utils.FirebaseUtils;
import com.devsz.studenteventsapi.dto.DataRequest;
import com.devsz.studenteventsapi.dto.PathRequest;
import com.devsz.studenteventsapi.entity.UserEntity;
import com.devsz.studenteventsapi.service.IUserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN') or hasRole('STUDENT') or hasRole('DEV')")
public class UserController {

    private final IUserService service;

    @PutMapping("/{id}s")
    public ResponseEntity<UserEntity> update(
            @PathVariable String id,
            @Valid @RequestBody DataRequest<UserEntity> data, Principal principal) throws Exception {

        if (FirebaseUtils.checkOwnership(id, principal.getName())) {
            return new ResponseEntity<>(service.update(data.path(), id, data.entity()), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);

    }

    @GetMapping
    public ResponseEntity<List<UserEntity>> readAll(
            @RequestBody PathRequest pathRequest) throws Exception {
        return new ResponseEntity<>(service.readAll(pathRequest), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserEntity> readById(
            @PathVariable String id,
            @RequestBody PathRequest pathRequest, Principal principal) throws Exception {
        if (FirebaseUtils.checkOwnership(id, principal.getName())) {
            return new ResponseEntity<>(service.readById(pathRequest, id), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable String id,
            @Valid @RequestBody PathRequest pathRequest, Principal principal) throws Exception {
        if (FirebaseUtils.checkOwnership(id, principal.getName())) {
            service.delete(pathRequest, id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }
}
