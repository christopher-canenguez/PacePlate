package com.paceplate.backend.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;

import com.paceplate.backend.dto.RecipeGenerationRequest;
import com.paceplate.backend.dto.RecipeGenerationResponse;
import com.paceplate.backend.model.Goal;

class RecipeServiceTest {

    private final RecipeService recipeService = new RecipeService();

    @Test
    void shouldReturnSuccessWhenTargetsAreMet() {

        RecipeGenerationRequest request = new RecipeGenerationRequest(
                List.of("chicken breast", "rice", "broccoli"),
                650,
                45.0,
                Goal.CUT,
                1);

        RecipeGenerationResponse response = recipeService.generateRecipe(request);

        assertEquals("SUCCESS", response.status());
        assertTrue(response.targetsMet());
        assertTrue(response.suggestions().isEmpty());
    }

    @Test
    void shouldReturnAlternativeWhenCaloriesExceeded() {

        RecipeGenerationRequest request = new RecipeGenerationRequest(
                List.of("chicken breast", "rice", "broccoli"),
                400,
                45.0,
                Goal.CUT,
                1);

        RecipeGenerationResponse response = recipeService.generateRecipe(request);

        assertEquals("ALTERNATIVE", response.status());
        assertFalse(response.targetsMet());
        assertEquals(1, response.suggestions().size());
        assertEquals("maxCalories", response.suggestions().get(0).field());
    }

    @Test
    void shouldReturnAlternativeWhenProteinTargetNotMet() {

        RecipeGenerationRequest request = new RecipeGenerationRequest(
                List.of("chicken, rice, broccoli"),
                650,
                80.0,
                Goal.CUT,
                1);

        RecipeGenerationResponse response = recipeService.generateRecipe(request);

        assertEquals("ALTERNATIVE", response.status());
        assertFalse(response.targetsMet());
        assertEquals(1, response.suggestions().size());
        assertEquals("minProtein", response.suggestions().get(0).field());
    }

    @Test
    void shouldReturnAlternativeWhenBothTargetsAreNotMet() {

        RecipeGenerationRequest request = new RecipeGenerationRequest(
                List.of("chicken, rice, broccoli"),
                400,
                80.0,
                Goal.CUT,
                1);

        RecipeGenerationResponse response = recipeService.generateRecipe(request);

        assertEquals("ALTERNATIVE", response.status());
        assertFalse(response.targetsMet());
        assertEquals(2, response.suggestions().size());
        assertEquals("maxCalories", response.suggestions().get(0).field());
        assertEquals("minProtein", response.suggestions().get(1).field());

    }
}
