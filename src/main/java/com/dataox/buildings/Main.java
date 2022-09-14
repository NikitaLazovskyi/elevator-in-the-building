package com.dataox.buildings;

import com.dataox.buildings.models.Building;
import com.dataox.buildings.models.Floor;
import com.dataox.buildings.models.People;

import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        Building building = new Building(true);
        try {
            building.getElevator().start();
        } catch (Exception e){
            System.out.println("-------------DONE-----------");
        }
//        Building building = new Building(false);
//        List<People> peopleList0 = new ArrayList<>();
//        peopleList0.addAll(List.of(
//                new People(3),
//                new People(2),
//                new People(1)));
//        List<People> peopleList1 = new ArrayList<>();
//        peopleList1.addAll(List.of(
//                new People(2),
//                new People(0)));
//        List<People> peopleList2 = new ArrayList<>();
//        peopleList2.addAll(List.of(
//                new People(3),
//                new People(3),
//                new People(1)));
//        List<People> peopleList3 = new ArrayList<>();
//        peopleList3.addAll(List.of(
//                new People(0),
//                new People(2),
//                new People(2)));
//        List<Floor> floors = new ArrayList<>();
//        floors.add(new Floor(peopleList0));
//        floors.add(new Floor(peopleList1));
//        floors.add(new Floor(peopleList2));
//        floors.add(new Floor(peopleList3));
//        building.setFloors(floors);
//        building.getElevator().start();
    }
}
