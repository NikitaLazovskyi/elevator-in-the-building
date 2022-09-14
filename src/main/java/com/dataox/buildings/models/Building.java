package com.dataox.buildings.models;

import com.dataox.buildings.generator.impl.GeneratorFloor;
import com.dataox.buildings.generator.impl.GeneratorPeople;
import lombok.Data;

import java.util.ArrayDeque;
import java.util.Iterator;
import java.util.List;
import java.util.stream.IntStream;

@Data
public class Building {
    // 5 >= x <= 20
    private List<Floor> floors;
    private Elevator elevator;
    private GeneratorFloor generatorFloor;
    private GeneratorPeople generatorPeople;
    private int allPeoples;
    private int illustrateCounter;

    public Building(boolean generate) {
        if (generate) {
            generatorFloor = new GeneratorFloor();
            // 5 20
//            floors = generatorFloor.generate(5, 20);
            floors = generatorFloor.generate(6, 6);
            elevator = new Elevator(this);
            allPeoples = (int) floors.stream().mapToLong(floor ->
                    floor.getFloorPeoples().size()
            ).sum();
        } else {
            elevator = new Elevator(this);
        }
    }

    public void setFloors(List<Floor> floors) {
        this.floors = floors;
        allPeoples = (int) floors.stream().mapToLong(floor ->
                floor.getFloorPeoples().size()
        ).sum();
    }

    @Data
    public static class Elevator {
        private final int maximumCapacity = 5;

        private final ArrayDeque<People> elevatorsPeoples;

        private int destinationElevatorFloor = 0;

        private boolean movingUp;

        private int currentFloor;

        private Building building;

        public Elevator(Building building) {
            this.building = building;
            this.elevatorsPeoples = new ArrayDeque<>(maximumCapacity);
        }

        public void start() {
            movingUp = true;
            currentFloor = 0;
            for (int i = 0; i < 100; i++) {
                print();
                checkDirection();
                throwOutPeoples();
                populateElevator();
                move();
            }
        }

        private void throwOutPeoples() {
            Iterator<People> iterator = elevatorsPeoples.iterator();
            while (iterator.hasNext()) {
                People people = iterator.next();
                if (people.getDestinationFloor() == currentFloor) {
                    building.floors.get(currentFloor).getReleasedPeople().add(people);
                    iterator.remove();
                }
            }
        }

        private void move() {
            if (movingUp) {
                currentFloor++;
            } else {
                currentFloor--;
            }
        }

        private void checkDirection() {
            if (currentFloor == building.floors.size() - 1) {
                movingUp = false;
            } else if (currentFloor == 0) {
                movingUp = true;
            }
        }

        private void populateElevator() {
            checkDirection();
            List<People> floorPeoples = building.floors.get(currentFloor).getFloorPeoples();
            if (hasFreeSpace() && building.floors.get(currentFloor).getFloorPeoples().size() > 0) {
                for (Iterator<People> iterator = floorPeoples.iterator(); iterator.hasNext(); ) {
                    People people = iterator.next();
                    if (movingUp && people.getDestinationFloor() > currentFloor && hasFreeSpace() ||
                            !movingUp && people.getDestinationFloor() < currentFloor && hasFreeSpace()) {
                        iterator.remove();
                        elevatorsPeoples.add(people);

//                        if (movingUp && people.getDestinationFloor() > destinationElevatorFloor ||
//                                !movingUp && people.getDestinationFloor() < destinationElevatorFloor) {
//                            destinationElevatorFloor = people.getDestinationFloor();
//                        }
                    }
                }
            }
        }

        private boolean hasFreeSpace() {
            return elevatorsPeoples.size() < maximumCapacity;
        }

        private void print() {
            System.out.printf("\n\n%51sSTEP %d\n", " ", building.illustrateCounter);
            int maximumPeoples = 10;
            StringBuilder sb = new StringBuilder();
            sb.append(String.format("%42s|%8s%4s%8s|\n", "", "________", "roof", "________"));
            for (int floorNumber = building.floors.size() - 1; floorNumber >= 0; floorNumber--) {
                IntStream.range(0, maximumPeoples - building.floors.get(floorNumber).getFloorPeoples().size()).forEach(
                        x -> sb.append("    "));
                building.floors.get(floorNumber).getFloorPeoples().forEach(
                        people -> sb.append(String.format(" %2d ", people.getDestinationFloor())));
                sb.append("->");
                if (currentFloor == floorNumber) {
                    sb.append("|");
                    elevatorsPeoples.forEach(people -> {
                        sb.append(String.format("[%2d]", people.getDestinationFloor()));
                    });
                    IntStream.range(0, maximumCapacity - elevatorsPeoples.size()).forEach(x -> sb.append(String.format("[%2s]", " ")));
                    sb.append("|");
                } else {
                    sb.append(String.format("|%20s|", " "));
                }
                sb.append(floorNumber);
                sb.append("|->");
                building.floors.get(floorNumber).getReleasedPeople().forEach(people ->{
                    sb.append(String.format(" %2d ", people.getDestinationFloor()));
                });
                sb.append("\n");
            }
            System.out.println(sb);
            building.illustrateCounter++;
        }
    }
}
