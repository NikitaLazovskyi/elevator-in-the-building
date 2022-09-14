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

    // 5 <= x < 20
    private final int floorsMax = 20;
    private final int floorsMin = 5;
    private List<Floor> floors;
    private Elevator elevator;
    private GeneratorFloor generatorFloor;
    private int allPeoples;
    private int illustrateCounter;

    public Building(boolean generate) {
        if (generate) {
            generatorFloor = new GeneratorFloor();
            floors = generatorFloor.generate(floorsMin, floorsMax);
            elevator = new Elevator(this);
            allPeoples = floors.stream().mapToInt(floor -> floor.getFloorPeoples().size()).sum();
        } else {
            elevator = new Elevator(this);
        }
    }

    public void setFloors(List<Floor> floors) {
        this.floors = floors;
        allPeoples = floors.stream().mapToInt(floor -> floor.getFloorPeoples().size()).sum();
    }

    @Data
    public static class Elevator {
        private final int maximumCapacity = 5;

        private final ArrayDeque<People> elevatorsPeoples;

        private int destinationElevatorFloor = 0;

        private boolean movingUp;
        private boolean isReachedDestination;

        private int currentFloor;

        private Building building;

        public Elevator(Building building) {
            this.building = building;
            this.elevatorsPeoples = new ArrayDeque<>(maximumCapacity);
        }

        public void start() {
            movingUp = true;
            currentFloor = 0;
            print();
            while (true) {
                checkDestinationFloor();
                checkDirection();
                throwOutPeoples();
                populateElevator();
                move();
                if (building.allPeoples == 0 && elevatorsPeoples.size() == 0) {
                    break;
                }
                print();
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

        private void checkDestinationFloor() {
            if (elevatorsPeoples.size() == 0) {
                isReachedDestination = true;
            } else {
                isReachedDestination = false;
            }

        }

        private void populateElevator() {
            checkDirection();
            List<People> floorPeoples = building.floors.get(currentFloor).getFloorPeoples();
            if (hasFreeSpace() && building.floors.get(currentFloor).getFloorPeoples().size() > 0) {
                for (Iterator<People> iterator = floorPeoples.iterator(); iterator.hasNext(); ) {
                    People people = iterator.next();
                    if (elevatorsPeoples.size() > 0
                            && ((elevatorsPeoples.getFirst().getDestinationFloor() > currentFloor && people.getDestinationFloor() < currentFloor)
                            || (elevatorsPeoples.getFirst().getDestinationFloor() < currentFloor && people.getDestinationFloor() > currentFloor))) {
                        continue;
                    }
                    if (movingUp && people.getDestinationFloor() > currentFloor && hasFreeSpace()
                            || !movingUp && people.getDestinationFloor() < currentFloor && hasFreeSpace()
                            || isReachedDestination) {
                        iterator.remove();
                        elevatorsPeoples.add(people);
                        building.allPeoples--;

                        if (movingUp && people.getDestinationFloor() > destinationElevatorFloor
                                || !movingUp && people.getDestinationFloor() < destinationElevatorFloor) {
                            destinationElevatorFloor = people.getDestinationFloor();
                            isReachedDestination = false;
                        }

                        if (isReachedDestination) {
                            movingUp = currentFloor < destinationElevatorFloor;
                        }
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
                List<People> floorPeoples = building.floors.get(floorNumber).getFloorPeoples();
                IntStream.range(0, maximumPeoples - floorPeoples.size()).forEach(x -> sb.append("    "));
                floorPeoples.forEach(people -> sb.append(String.format(" %2d ", people.getDestinationFloor())));
                sb.append("->");
                if (currentFloor == floorNumber) {
                    sb.append("|");
                    elevatorsPeoples.forEach(people -> {
                        sb.append(String.format("[%2d]", people.getDestinationFloor()));
                    });
                    IntStream.range(0, maximumCapacity - elevatorsPeoples.size()).forEach(
                            x -> sb.append(String.format("[%2s]", " ")));
                    sb.append("|");
                } else {
                    sb.append(String.format("|%20s|", " "));
                }
                sb.append(String.format("%2d", floorNumber));
                sb.append("|->");
                building.floors.get(floorNumber).getReleasedPeople().forEach(people -> {
                    sb.append(String.format(" %2d ", people.getDestinationFloor()));
                });
                sb.append("\n");
            }
            System.out.println(sb);
            building.illustrateCounter++;
        }
    }
}
