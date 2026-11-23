package com.paceplate.backend.dto;

public record IngredientResponse(
                String name,
                double quantity,
                String unit,
                String foodState) {
}
