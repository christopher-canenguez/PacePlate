package com.paceplate.backend.controller;

import com.paceplate.backend.dto.RecipeGenerationRequest;
import com.paceplate.backend.service.RecipeService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/recipes")
public class RecipeController {

    private final RecipeService recipeService;

    public RecipeController(RecipeService recipeService) {
        this.recipeService = recipeService;
    }

    @PostMapping("/generate")
    public Map<String, Object> generateRecipe(@Valid @RequestBody RecipeGenerationRequest request) {
        return recipeService.generateRecipe(request);
    }
}
