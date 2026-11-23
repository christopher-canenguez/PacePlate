package com.paceplate.backend.dto;

import java.util.List;

public record RecipeResponse(
        String name,
        String description,
        int servings,
        int prepTimeMinutes,
        int cookTimeMinutes,
        List<IngredientResponse> ingredients,
        NutritionResponse nutrition,
        List<String> instructions) {
}
