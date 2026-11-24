package com.paceplate.backend.dto.usda;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record UsdaFoodResponse(
        Integer fdcId,
        String description,
        String dataType,
        List<UsdaNutrientResponse> foodNutrients) {
}
