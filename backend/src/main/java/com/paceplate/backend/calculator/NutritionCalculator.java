package com.paceplate.backend.calculator;

import java.util.List;

import org.springframework.stereotype.Component;

import com.paceplate.backend.dto.NutritionResponse;

@Component
public class NutritionCalculator {

    public NutritionResponse scaleNutrition(NutritionResponse nutritionPer100g, double weightInGrams) {

        if (!Double.isFinite(weightInGrams) || weightInGrams < 0) {
            throw new IllegalArgumentException("Ingredient weight must be finite and non-negative");
        }

        double scalingFactor = weightInGrams / 100.0;

        return new NutritionResponse(
                nutritionPer100g.calories() * scalingFactor,
                nutritionPer100g.protein() * scalingFactor,
                nutritionPer100g.carbs() * scalingFactor,
                nutritionPer100g.fat() * scalingFactor);
    }

    public NutritionResponse combineNutrition(List<NutritionResponse> ingredientNutrition) {

        double calories = 0;
        double protein = 0;
        double carbs = 0;
        double fat = 0;

        for (NutritionResponse nutrition : ingredientNutrition) {
            calories += nutrition.calories();
            protein += nutrition.protein();
            carbs += nutrition.carbs();
            fat += nutrition.fat();
        }

        return new NutritionResponse(
                calories,
                protein,
                carbs,
                fat);
    }

    public NutritionResponse calculatePerServing(
            NutritionResponse totalNutrition,
            int servings) {

        if (servings < 1) {
            throw new IllegalArgumentException(
                    "Number of servings must be at least 1");
        }

        return new NutritionResponse(
                totalNutrition.calories() / servings,
                totalNutrition.protein() / servings,
                totalNutrition.carbs() / servings,
                totalNutrition.fat() / servings);
    }

}
