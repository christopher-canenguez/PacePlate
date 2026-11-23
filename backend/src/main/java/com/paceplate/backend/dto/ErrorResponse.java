package com.paceplate.backend.dto;

import java.util.List;

public record ErrorResponse(
        String status,
        String code,
        String message,
        List<String> details) {
}
