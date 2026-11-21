package com.paceplate.backend.service;

import com.paceplate.backend.dto.RecipeGenerationRequest;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class RecipeService {

    public Map<String, Object> generateRecipe(RecipeGenerationRequest request) {

        return Map.of(
                "message", "Recipe request received successfully",
                "ingredientCount", request.ingredients().size(),
                "maxCalories", request.maxCalories(),
                "minProtein", request.minProtein(),
                "goal", request.goal(),
                "servings", request.servings());
    }
}
