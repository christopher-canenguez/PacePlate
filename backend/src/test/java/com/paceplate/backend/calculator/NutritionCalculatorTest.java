package com.paceplate.backend.calculator;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import com.paceplate.backend.dto.NutritionResponse;

import static org.junit.jupiter.api.Assertions.*;

class NutritionCalculatorTest {

    private final NutritionCalculator nutritionCalculator = new NutritionCalculator();

    private static final double DELTA = 0.000001;

    private void assertNutrition(
            NutritionResponse actual,
            double expectedCalories,
            double expectedProtein,
            double expectedCarbs,
            double expectedFat) {

        assertAll(
                () -> assertEquals(expectedCalories, actual.calories(), DELTA),
                () -> assertEquals(expectedProtein, actual.protein(), DELTA),
                () -> assertEquals(expectedCarbs, actual.carbs(), DELTA),
                () -> assertEquals(expectedFat, actual.fat(), DELTA));
    }

    @Test
    void shouldScaleNutritionAbove100Grams() {

        // Arrange
        NutritionResponse reference = new NutritionResponse(120, 22.5, 0, 2.6);

        // Act
        NutritionResponse result = nutritionCalculator.scaleNutrition(reference, 200);

        // Assert
        assertNutrition(result, 240, 45, 0, 5.2);
    }

    @Test
    void shouldScaleNutritionBelow100Grams() {

        NutritionResponse reference = new NutritionResponse(365, 7, 80, 0.7);

        NutritionResponse result = nutritionCalculator.scaleNutrition(reference, 75);

        assertNutrition(result, 273.75, 5.25, 60, 0.525);
    }

    @Test
    void shouldReturnZeroNutritionForZeroWeight() {

        NutritionResponse reference = new NutritionResponse(120, 22.5, 0, 2.6);

        NutritionResponse result = nutritionCalculator.scaleNutrition(reference, 0);

        assertNutrition(result, 0, 0, 0, 0);
    }

    @ParameterizedTest
    @ValueSource(doubles = {
            -100.0,
            -1.0,
            Double.NaN,
            Double.POSITIVE_INFINITY,
            Double.NEGATIVE_INFINITY
    })
    void shouldRejectInvalidWeight(double weight) {

        NutritionResponse reference = new NutritionResponse(120, 22.5, 0, 2.6);

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> nutritionCalculator.scaleNutrition(
                        reference, weight));

        assertEquals("Ingredient weight must be finite and non-negative", exception.getMessage());
    }

    @Test
    void shouldCombineMultipleIngredients() {

        List<NutritionResponse> ingredients = List.of(
                new NutritionResponse(240, 45, 0, 5.2),
                new NutritionResponse(273.75, 5.25, 60, 0.525),
                new NutritionResponse(34, 2.8, 6.6, 0.4));

        NutritionResponse result = nutritionCalculator.combineNutrition(ingredients);

        assertNutrition(result, 547.75, 53.05, 66.6, 6.125);
    }

    @Test
    void shouldReturnZeroNutritionForEmptyList() {

        NutritionResponse result = nutritionCalculator.combineNutrition(List.of());

        assertNutrition(result, 0, 0, 0, 0);
    }

    @Test
    void shouldCalculateNutritionPerServing() {

        NutritionResponse total = new NutritionResponse(547.75, 53.05, 66.6, 6.125);

        NutritionResponse result = nutritionCalculator.calculatePerServing(total, 2);

        assertNutrition(result, 273.875, 26.525, 33.3, 3.0625);
    }

    @Test
    void shouldPreserveNutritionForOneServing() {

        NutritionResponse total = new NutritionResponse(547.75, 53.05, 66.6, 6.125);

        NutritionResponse result = nutritionCalculator.calculatePerServing(total, 1);

        assertNutrition(result, 547.75, 53.05, 66.6, 6.125);
    }

    @ParameterizedTest
    @ValueSource(ints = { 0, -1, -5 })
    void shouldRejectInvalidServings(int servings) {

        NutritionResponse total = new NutritionResponse(800, 80, 60, 20);

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> nutritionCalculator.calculatePerServing(
                        total, servings));

        assertEquals("Number of servings must be at least 1", exception.getMessage());
    }

    @Test
    void shouldCalculateCompleteRecipeNutrition() {

        // Reference nutrition per 100g
        NutritionResponse chicken = new NutritionResponse(120, 22.5, 0, 2.6);

        NutritionResponse rice = new NutritionResponse(365, 7, 80, 0.7);

        NutritionResponse broccoli = new NutritionResponse(34, 2.8, 6.6, 0.4);

        // Scale each ingredient
        NutritionResponse scaledChicken = nutritionCalculator.scaleNutrition(chicken, 200);

        NutritionResponse scaledRice = nutritionCalculator.scaleNutrition(rice, 75);

        NutritionResponse scaledBroccoli = nutritionCalculator.scaleNutrition(broccoli, 100);

        // Combine ingredients
        NutritionResponse total = nutritionCalculator.combineNutrition(
                List.of(
                        scaledChicken,
                        scaledRice,
                        scaledBroccoli));

        // Calculate per serving
        NutritionResponse result = nutritionCalculator.calculatePerServing(total, 2);

        assertNutrition(result, 273.875, 26.525, 33.3, 3.0625);
    }
}
