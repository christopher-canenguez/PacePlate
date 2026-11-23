package com.paceplate.backend.dto;

public record NutritionResponse(
        double calories,
        double protein,
        double carbs,
        double fat) {
}
