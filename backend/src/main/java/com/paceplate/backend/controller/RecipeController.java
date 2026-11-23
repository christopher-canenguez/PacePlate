package com.paceplate.backend.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.paceplate.backend.dto.RecipeGenerationRequest;
import com.paceplate.backend.dto.RecipeGenerationResponse;
import com.paceplate.backend.service.RecipeService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/recipes")
public class RecipeController {

    private final RecipeService recipeService;

    public RecipeController(RecipeService recipeService) {
        this.recipeService = recipeService;
    }

    @PostMapping("/generate")
    public RecipeGenerationResponse generateRecipe(@Valid @RequestBody RecipeGenerationRequest request) {
        return recipeService.generateRecipe(request);
    }
}
