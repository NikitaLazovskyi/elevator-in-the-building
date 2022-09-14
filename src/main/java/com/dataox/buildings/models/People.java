package com.dataox.buildings.models;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
public class People{
    private int destinationFloor;

    @Override
    public String toString() {
        return "People{" +
                "dest=" + destinationFloor +
                '}';
    }

//    @Override
//    public int compareTo(People o) {
//        return 0;
//    }
}
