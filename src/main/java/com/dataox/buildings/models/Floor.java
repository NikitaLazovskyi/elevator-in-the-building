package com.dataox.buildings.models;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class Floor {
    private final List<People> floorPeoples;
    private List<People> releasedPeople;
    private int numberOfFloor;

    public Floor(List<People> floorPeoples, int numberOfFloor) {
        this.floorPeoples = floorPeoples;
        this.numberOfFloor = numberOfFloor;
        this.releasedPeople = new ArrayList<>();
    }

    @Override
    public String toString() {
        return "\nFloor{" +
                "\n\tfloorPeoples=" + floorPeoples +
                '}';
    }
}
