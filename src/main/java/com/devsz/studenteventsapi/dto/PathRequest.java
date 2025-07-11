package com.devsz.studenteventsapi.dto;

import jakarta.validation.constraints.NotBlank;

public record PathRequest(
        @NotBlank(message = "Path cannot be empty")
        String[] pathSegments
 ) {
}