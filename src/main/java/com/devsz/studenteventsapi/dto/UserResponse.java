package com.devsz.studenteventsapi.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;

public record UserResponse(
    String id,
    String username,
    String email,
    String role,
    String token,

    @JsonIgnore
    Long createdAt

) {

}
