package com.devsz.studenteventsapi.dto;

public record DataRequest<T>(
    PathRequest path,
    T entity
) {
}
