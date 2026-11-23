package com.paceplate.backend.service;

import com.paceplate.backend.dto.AdjustmentSuggestionResponse;
import com.paceplate.backend.dto.IngredientResponse;
import com.paceplate.backend.dto.NutritionResponse;
import com.paceplate.backend.dto.RecipeGenerationRequest;
import com.paceplate.backend.dto.RecipeGenerationResponse;
import com.paceplate.backend.dto.RecipeResponse;

import java.util.List;

import org.springframework.stereotype.Service;

@Service
public class RecipeService {

    public RecipeGenerationResponse generateRecipe(RecipeGenerationRequest request) {

        List<IngredientResponse> ingredients = List.of(
                new IngredientResponse("Chicken breast", 200, "g", "RAW"),
                new IngredientResponse("White rice", 75, "g", "DRY"),
                new IngredientResponse("Broccoli", 100, "g", "RAW"));

        NutritionResponse nutrition = new NutritionResponse(
                550,
                52,
                55,
                12);

        RecipeResponse recipe = new RecipeResponse(
                "Chicken and Rice Bowl",
                "A high-protein meal with chicken, rice, and broccoli.",
                1,
                10,
                25,
                ingredients,
                nutrition,
                List.of(
                        "Cook the rice according to package instructions.",
                        "Season and cook the chicken thoroughly.",
                        "Steam the broccoli.",
                        "Combine the ingredients and serve."));

        boolean calorieTargetMet = meetsCalorieTarget(nutrition, request);
        boolean proteinTargetMet = meetsProteinTarget(nutrition, request);

        boolean targetsMet = calorieTargetMet && proteinTargetMet;

        List<AdjustmentSuggestionResponse> suggestions = new java.util.ArrayList<>();

        if (!calorieTargetMet) {
            suggestions.add(new AdjustmentSuggestionResponse(
                    "maxCalories",
                    request.maxCalories(),
                    nutrition.calories(),
                    "Increase your calorie limit to accommodate this recipe."));
        }

        if (!proteinTargetMet) {
            suggestions.add(new AdjustmentSuggestionResponse(
                    "minProtein",
                    request.minProtein(),
                    nutrition.protein(),
                    "Lower your minimum protein target to accommodate this recipe."));
        }

        return new RecipeGenerationResponse(
                targetsMet ? "SUCCESS" : "ALTERNATIVE",
                targetsMet,
                recipe,
                suggestions);

    }

    private boolean meetsCalorieTarget(NutritionResponse nutrition, RecipeGenerationRequest request) {
        return nutrition.calories() <= request.maxCalories();
    }

    private boolean meetsProteinTarget(NutritionResponse nutrition, RecipeGenerationRequest request) {
        return nutrition.protein() >= request.minProtein();
    }
}
