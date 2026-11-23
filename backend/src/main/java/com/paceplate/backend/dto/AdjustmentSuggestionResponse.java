package com.paceplate.backend.dto;

public record AdjustmentSuggestionResponse(
        String field,
        double currentValue,
        double suggestedValue,
        String reason) {
}
