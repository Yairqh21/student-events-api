package com.devsz.studenteventsapi.dto;

public record UserResponse(
    UserRequest user,
    String token

) {

}
