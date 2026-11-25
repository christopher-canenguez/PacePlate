package com.paceplate.backend.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;

import org.junit.jupiter.api.Test;

import com.paceplate.backend.dto.NutritionResponse;
import com.paceplate.backend.dto.usda.UsdaFoodResponse;
import com.paceplate.backend.dto.usda.UsdaNutrientResponse;

class NutritionMapperTest {
    private final NutritionMapper nutritionMapper = new NutritionMapper();

    @Test
    void shouldMapAllRequiredNutrients() {

        UsdaFoodResponse food = createFood(List.of(
                createNutrient(1008, "KCAL", 120.0),
                createNutrient(1003, "G", 22.5),
                createNutrient(1005, "G", 1.0),
                createNutrient(1004, "G", 2.6)));

        NutritionResponse result = nutritionMapper.toNutritionResponse(food);

        assertEquals(120.0, result.calories());
        assertEquals(22.5, result.protein());
        assertEquals(1.0, result.carbs());
        assertEquals(2.6, result.fat());
    }

    @Test
    void shouldAcceptZeroNutrientValues() {

        UsdaFoodResponse food = createFood(List.of(
                createNutrient(1008, "KCAL", 120.0),
                createNutrient(1003, "G", 22.5),
                createNutrient(1005, "G", 0.0),
                createNutrient(1004, "G", 2.6)));

        NutritionResponse result = nutritionMapper.toNutritionResponse(food);

        assertEquals(0.0, result.carbs());
    }

    @Test
    void shouldThrowExceptionWhenProteinIsMissing() {

        UsdaFoodResponse food = createFood(List.of(
                createNutrient(1008, "KCAL", 120.0),
                createNutrient(1005, "G", 0.0),
                createNutrient(1004, "G", 2.6)));

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> nutritionMapper.toNutritionResponse(food));

        assertEquals("Missing nutrient: PROTEIN", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenNutrientValueIsNull() {

        UsdaFoodResponse food = createFood(List.of(
                createNutrient(1008, "KCAL", 120.0),
                createNutrient(1003, "G", null),
                createNutrient(1005, "G", 0.0),
                createNutrient(1004, "G", 2.6)));

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> nutritionMapper.toNutritionResponse(food));

        assertEquals("Missing nutrient: PROTEIN", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenNutrientUnitIsIncorrect() {

        UsdaFoodResponse food = createFood(List.of(
                createNutrient(1008, "KCAL", 120.0),
                createNutrient(1003, "MG", 22500.0),
                createNutrient(1005, "G", 0.0),
                createNutrient(1004, "G", 2.6)));

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> nutritionMapper.toNutritionResponse(food));

        assertEquals("Missing nutrient: PROTEIN", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenNutrientListIsEmpty() {
        UsdaFoodResponse food = createFood(List.of());

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> nutritionMapper.toNutritionResponse(food));

        assertEquals("Food contains no nutritional data", exception.getMessage());

    }

    @Test
    void shouldThrowExceptionWhenNutrientListIsNull() {
        UsdaFoodResponse food = createFood(null);

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> nutritionMapper.toNutritionResponse(food));

        assertEquals("Food contains no nutritional data", exception.getMessage());
    }

    @Test
    void shouldIgnoreUnnecessaryNutrients() {
        UsdaFoodResponse food = createFood(List.of(
                createNutrient(1008, "KCAL", 120.0),
                createNutrient(1003, "G", 22.5),
                createNutrient(1005, "G", 1.0),
                createNutrient(1004, "G", 2.6),
                createNutrient(1087, "G", 1.0),
                createNutrient(1093, "G", 2.5)));

        NutritionResponse result = nutritionMapper.toNutritionResponse(food);

        assertEquals(120.0, result.calories());
        assertEquals(22.5, result.protein());
        assertEquals(1.0, result.carbs());
        assertEquals(2.6, result.fat());
    }

    private UsdaFoodResponse createFood(List<UsdaNutrientResponse> nutrients) {

        return new UsdaFoodResponse(
                123456,
                "Test food",
                "Foundation",
                nutrients);
    }

    private UsdaNutrientResponse createNutrient(int id, String unit, Double value) {

        return new UsdaNutrientResponse(
                id,
                "Test nutrient",
                unit,
                value);
    }
}
