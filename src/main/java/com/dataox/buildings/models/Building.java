package com.dataox.buildings.models;

import com.dataox.buildings.exception.NoFloorsWithPeopleException;
import com.dataox.buildings.generator.impl.GeneratorFloor;
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
            try {
                while (true) {
                    checkDestinationFloor();
                    checkDirection();
                    throwOutPeoples();
                    populateElevator();
                    move();
                    print();
                }
            } catch (NoFloorsWithPeopleException e) {
                System.out.println(e.getMessage());
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
            checkDirection();
            checkDestinationFloor();
        }

        private void move() {
            if (elevatorsPeoples.size() == 0 && building.floors.get(currentFloor).getFloorPeoples().size() == 0) {
                int numberOfFloor = building.floors.stream().filter(
                        floor -> floor.getFloorPeoples().size() > 0).findAny().orElseThrow(
                        () -> new NoFloorsWithPeopleException("All peoples were delivered")
                ).getNumberOfFloor();
                movingUp = numberOfFloor > currentFloor;
                destinationElevatorFloor = numberOfFloor;
                checkDirection();
            }
            if (movingUp) {
                currentFloor++;
            } else {
                currentFloor--;
            }
        }

        private void checkDirection() {
            movingUp = destinationElevatorFloor > currentFloor;
            if (currentFloor == building.floors.size() - 1) {
                movingUp = false;
            } else if (currentFloor == 0) {
                movingUp = true;
            }
        }

        private void checkDestinationFloor() {
            isReachedDestination = elevatorsPeoples.size() == 0;

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
                        building.allPeoples--;
                        elevatorsPeoples.add(people);
                        movingUp = people.getDestinationFloor() > currentFloor;

                        Integer temp;
                        if (movingUp) {
                            temp = elevatorsPeoples.stream().mapToInt(People::getDestinationFloor)
                                    .boxed().max(Integer::compare).orElse(null);
                        } else {
                            temp = elevatorsPeoples.stream().mapToInt(People::getDestinationFloor)
                                    .boxed().min(Integer::compare).orElse(null);
                        }
                        if (temp != null) {
                            destinationElevatorFloor = temp;
                        }
                        isReachedDestination = false;
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
            sb.append(String.format("%42s|%9s%4s%9s|\n", "", "________", "roof", "________"));
            for (int floorNumber = building.floors.size() - 1; floorNumber >= 0; floorNumber--) {
                List<People> floorPeoples = building.floors.get(floorNumber).getFloorPeoples();
                IntStream.range(0, maximumPeoples - floorPeoples.size()).forEach(x -> sb.append("    "));
                floorPeoples.forEach(people -> sb.append(String.format(" %2d ", people.getDestinationFloor())));
                sb.append("->");
                if (currentFloor == floorNumber) {
                    sb.append("|");
                    if (movingUp) {
                        sb.append("↑");
                    }else{
                        sb.append("↓");
                    }
                    elevatorsPeoples.forEach(people -> {
                        sb.append(String.format("[%2d]", people.getDestinationFloor()));
                    });
                    IntStream.range(0, maximumCapacity - elevatorsPeoples.size()).forEach(
                            x -> sb.append(String.format("[%2s]", " ")));
                    if (movingUp) {
                        sb.append("↑");
                    }else{
                        sb.append("↓");
                    }
                    sb.append("|");
                } else {
                    sb.append(String.format("|%22s|", " "));
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
