package com.paceplate.backend.model;

public enum Nutrient {

    CALORIES(1008, "KCAL"),
    PROTEIN(1003, "G"),
    CARBOHYDRATES(1005, "G"),
    FAT(1004, "G");

    private final int id;
    private final String unit;

    Nutrient(int id, String unit) {
        this.id = id;
        this.unit = unit;
    }

    public int getId() {
        return id;
    }

    public String getUnit() {
        return unit;
    }
}
