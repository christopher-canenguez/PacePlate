package com.paceplate.backend.dto;

import java.util.List;

public record RecipeGenerationResponse(
                String status,
                boolean targetsMet,
                RecipeResponse recipe,
                List<AdjustmentSuggestionResponse> suggestions) {
}
