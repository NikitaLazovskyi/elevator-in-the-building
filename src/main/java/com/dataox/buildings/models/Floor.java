package com.dataox.buildings.models;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class Floor {
    // 5 >= x <= 20
    private final List<People> floorPeoples;
    private List<People> releasedPeople;

    public Floor(List<People> floorPeoples) {
        this.floorPeoples = floorPeoples;
        this.releasedPeople = new ArrayList<>();
    }

    @Override
    public String toString() {
        return "\nFloor{" +
                "\n\tfloorPeoples=" + floorPeoples +
                '}';
    }
}
