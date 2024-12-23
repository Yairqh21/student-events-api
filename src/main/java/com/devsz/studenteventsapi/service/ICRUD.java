package com.devsz.studenteventsapi.service;

import com.devsz.studenteventsapi.dto.PathRequest;

import java.util.List;
import java.util.concurrent.ExecutionException;

public interface ICRUD<T, ID> {

    T save(PathRequest pathRequest, T data) throws ExecutionException, InterruptedException;

    T update(PathRequest pathRequest, ID id, T data) throws ExecutionException, InterruptedException, IllegalAccessException;

    List<T> readAll(PathRequest pathRequest) throws ExecutionException, InterruptedException;

    T readById(PathRequest pathRequest, ID id) throws ExecutionException, InterruptedException;

    void delete(PathRequest pathRequest, ID id) throws ExecutionException, InterruptedException;
}
