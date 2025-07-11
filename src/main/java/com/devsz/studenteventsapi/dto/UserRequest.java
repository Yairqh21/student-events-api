package com.devsz.studenteventsapi.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.cloud.Timestamp;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class UserRequest {
    private String id;
    private String email;
    private String password;
    private String username;
    private String role;
    @JsonIgnore
    private Timestamp createdAt;
}
