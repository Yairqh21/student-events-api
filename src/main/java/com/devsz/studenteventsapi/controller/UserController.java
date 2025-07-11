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
// @PreAuthorize("hasRole('ADMIN') or hasRole('STUDENT') or hasRole('DEV')")
public class UserController {

    private final IUserService service;

    @PutMapping("/me")
    public ResponseEntity<UserEntity> update(
            @Valid @RequestBody DataRequest<UserEntity> data, Principal principal) throws Exception {

        if (principal == null || principal.getName() == null) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        return new ResponseEntity<>(service.update(data.path(), principal.getName(), data.entity()), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<UserEntity>> readAll(
            @RequestParam("pathSegments") List<String> pathSegments) throws Exception {
        PathRequest pathRequest = new PathRequest(pathSegments.toArray(new String[0]));
        return new ResponseEntity<>(service.readAll(pathRequest), HttpStatus.OK);
    }

    @GetMapping("/me")
    public ResponseEntity<UserEntity> readUserInfo(
            @RequestParam("pathSegments") List<String> pathSegments,
            Principal principal) throws Exception {

        if (principal == null || principal.getName() == null) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        PathRequest pathRequest = new PathRequest(pathSegments.toArray(new String[0]));
        return new ResponseEntity<>(service.readById(pathRequest, principal.getName()), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserEntity> readById(
            @RequestParam("pathSegments") List<String> pathSegments, @PathVariable String id) throws Exception {

        PathRequest pathRequest = new PathRequest(pathSegments.toArray(new String[0]));
        return new ResponseEntity<>(service.readById(pathRequest, id), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable String id,
            @RequestParam("pathSegments") List<String> pathSegments, Principal principal) throws Exception {

        if (principal == null || principal.getName() == null ||
                !FirebaseUtils.checkOwnership(id, principal.getName())) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        PathRequest pathRequest = new PathRequest(pathSegments.toArray(new String[0]));
        service.delete(pathRequest, id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}