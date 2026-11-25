package com.paceplate.backend.mapper;

import java.util.List;

import org.springframework.stereotype.Component;

import com.paceplate.backend.dto.usda.UsdaNutrientResponse;
import com.paceplate.backend.model.Nutrient;
import com.paceplate.backend.dto.NutritionResponse;
import com.paceplate.backend.dto.usda.UsdaFoodResponse;

@Component
public class NutritionMapper {

        public NutritionResponse toNutritionResponse(UsdaFoodResponse food) {

                List<UsdaNutrientResponse> nutrients = food.foodNutrients();

                if (nutrients == null || nutrients.isEmpty()) {
                        throw new IllegalArgumentException("Food contains no nutritional data");
                }

                double calories = extractNutrient(nutrients, Nutrient.CALORIES);

                double protein = extractNutrient(nutrients, Nutrient.PROTEIN);

                double carbs = extractNutrient(nutrients, Nutrient.CARBOHYDRATES);

                double fat = extractNutrient(nutrients, Nutrient.FAT);

                return new NutritionResponse(calories, protein, carbs, fat);
        }

        private double extractNutrient(List<UsdaNutrientResponse> nutrients, Nutrient target) {

                return nutrients.stream()
                                .filter(nutrient -> nutrient.nutrientId() != null
                                                && nutrient.nutrientId() == target.getId())
                                .filter(nutrient -> target.getUnit().equalsIgnoreCase(nutrient.unitName()))
                                .map(UsdaNutrientResponse::value)
                                .filter(value -> value != null)
                                .findFirst()
                                .orElseThrow(() -> new IllegalArgumentException("Missing nutrient: " + target.name()));
        }
}
