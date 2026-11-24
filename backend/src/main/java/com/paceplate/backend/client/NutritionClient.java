package com.paceplate.backend.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import com.paceplate.backend.dto.usda.UsdaSearchResponse;

@Component
public class NutritionClient {

    private final RestClient restClient;
    private final String apiKey;

    public NutritionClient(
            RestClient.Builder restClientBuilder,
            @Value("${usda.api.base-url}") String baseUrl,
            @Value("${usda.api.key}") String apiKey) {

        this.restClient = restClientBuilder
                .baseUrl(baseUrl)
                .build();

        this.apiKey = apiKey;
    }

    public UsdaSearchResponse searchFoods(String query) {

        return restClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/foods/search")
                        .queryParam("query", query)
                        .queryParam("pageSize", 5)
                        .queryParam("api_key", apiKey)
                        .build())
                .retrieve()
                .body(UsdaSearchResponse.class);
    }
}
