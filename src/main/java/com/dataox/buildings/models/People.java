package com.dataox.buildings.models;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class People {
    private int destinationFloor;

    @Override
    public String toString() {
        return "People{" +
                "dest=" + destinationFloor +
                '}';
    }
}
