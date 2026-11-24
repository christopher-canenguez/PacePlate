package com.paceplate.backend.dto.usda;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record UsdaNutrientResponse(
        Integer nutrientId,
        String nutrientName,
        String unitName,
        Double value) {
}
