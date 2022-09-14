package com.dataox.buildings;

import com.dataox.buildings.models.Building;

public class Main {
    public static void main(String[] args) {
        Building building = new Building(true);
        building.getElevator().start();
    }
}
