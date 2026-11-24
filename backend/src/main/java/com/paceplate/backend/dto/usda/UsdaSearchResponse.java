package com.paceplate.backend.dto.usda;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record UsdaSearchResponse(
        Integer totalHits,
        List<UsdaFoodResponse> foods) {
}
