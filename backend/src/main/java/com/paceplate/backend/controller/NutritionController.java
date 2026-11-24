package com.paceplate.backend.controller;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.paceplate.backend.client.NutritionClient;
import com.paceplate.backend.dto.usda.UsdaSearchResponse;

@RestController
@RequestMapping("/api/nutrition")
public class NutritionController {

    private final NutritionClient nutritionClient;

    public NutritionController(NutritionClient nutritionClient) {
        this.nutritionClient = nutritionClient;
    }

    @GetMapping(value = "/search", produces = MediaType.APPLICATION_JSON_VALUE)
    public UsdaSearchResponse searchFoods(@RequestParam String query) {
        return nutritionClient.searchFoods(query);
    }
}
