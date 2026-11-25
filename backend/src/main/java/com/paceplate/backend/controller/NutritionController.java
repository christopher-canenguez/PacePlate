package com.paceplate.backend.controller;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.paceplate.backend.client.NutritionClient;
import com.paceplate.backend.dto.usda.UsdaSearchResponse;
import com.paceplate.backend.mapper.NutritionMapper;
import com.paceplate.backend.dto.NutritionResponse;
import com.paceplate.backend.dto.usda.UsdaFoodResponse;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

@RestController
@RequestMapping("/api/nutrition")
public class NutritionController {

    private final NutritionClient nutritionClient;
    private final NutritionMapper nutritionMapper;

    public NutritionController(
            NutritionClient nutritionClient,
            NutritionMapper nutritionMapper) {

        this.nutritionClient = nutritionClient;
        this.nutritionMapper = nutritionMapper;
    }

    @GetMapping(value = "/search", produces = MediaType.APPLICATION_JSON_VALUE)
    public UsdaSearchResponse searchFoods(@RequestParam String query) {
        return nutritionClient.searchFoods(query);
    }

    @GetMapping("/macros")
    public NutritionResponse getMacros(@RequestParam String query) {

        UsdaSearchResponse response = nutritionClient.searchFoods(query);

        if (response == null || response.foods() == null || response.foods().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No matching foods found");
        }

        UsdaFoodResponse food = response.foods().getFirst();

        return nutritionMapper.toNutritionResponse(food);
    }
}
