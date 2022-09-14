package com.dataox.buildings.models;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class Floor {
    // 5 >= x <= 20
    private List<People> floorPeoples;

    @Override
    public String toString() {
        return "\nFloor{" +
                "\n\tfloorPeoples=" + floorPeoples +
                '}';
    }
}
