package com.paceplate.backend.dto;

import java.util.List;

import com.paceplate.backend.model.Goal;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.DecimalMin;

public record RecipeGenerationRequest(

    @NotEmpty
    List<String> ingredients,

    @NotNull
    @Min(1)
    Integer maxCalories,

    @NotNull
    @DecimalMin("0.0")
    Double minProtein,

    @NotNull
    Goal goal,

    @NotNull
    @Min(1)
    Integer servings

) {}
