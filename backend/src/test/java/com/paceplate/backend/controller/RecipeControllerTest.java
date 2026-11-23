package com.paceplate.backend.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.paceplate.backend.dto.NutritionResponse;
import com.paceplate.backend.dto.RecipeGenerationResponse;
import com.paceplate.backend.dto.RecipeResponse;
import com.paceplate.backend.service.RecipeService;

@WebMvcTest(RecipeController.class)
class RecipeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private RecipeService recipeService;

    @Test
    void shouldReturnSuccessForValidRequest() throws Exception {

        RecipeResponse recipe = new RecipeResponse(
                "Test Recipe",
                "A test recipe",
                1,
                10,
                20,
                List.of(),
                new NutritionResponse(500, 50, 40, 10),
                List.of("Cook ingredients"));

        RecipeGenerationResponse response = new RecipeGenerationResponse(
                "SUCCESS",
                true,
                recipe,
                List.of());

        when(recipeService.generateRecipe(any())).thenReturn(response);

        String requestJson = """
                {
                  "ingredients": ["chicken", "rice"],
                  "maxCalories": 650,
                  "minProtein": 45,
                  "goal": "CUT",
                  "servings": 1
                }
                """;

        mockMvc.perform(
                post("/api/recipes/generate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("SUCCESS"))
                .andExpect(jsonPath("$.targetsMet").value(true))
                .andExpect(jsonPath("$.recipe.name").value("Test Recipe"));
    }

    @Test
    void shouldReturnBadRequestForInvalidCalories() throws Exception {

        String requestJson = """
                {
                  "ingredients": ["chicken"],
                  "maxCalories": -100,
                  "minProtein": 45,
                  "goal": "CUT",
                  "servings": 1
                }
                """;

        mockMvc.perform(
                post("/api/recipes/generate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value("ERROR"))
                .andExpect(jsonPath("$.code").value("VALIDATION_ERROR"));
    }

    @Test
    void shouldReturnBadRequestForInvalidGoal() throws Exception {
        String requestJson =
        """
                {
                  "ingredients": ["chicken"],
                  "maxCalories": 650,
                  "minProtein": 45,
                  "goal": "PIZZA",
                  "servings": 1
                }
        """;

        mockMvc.perform(
                post("/api/recipes/generate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value("ERROR"))
                .andExpect(jsonPath("$.code").value("INVALID_REQUEST_BODY"));
    }
}
