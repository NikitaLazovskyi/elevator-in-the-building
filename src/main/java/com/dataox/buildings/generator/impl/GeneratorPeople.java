package com.dataox.buildings.generator.impl;

import com.dataox.buildings.generator.Generator;
import com.dataox.buildings.models.People;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

@Data
public class GeneratorPeople implements Generator<People> {
    private int floors;
    private int currentFloor;

    public GeneratorPeople(int floors, int currentFloor) {
        this.floors = floors;
        this.currentFloor = currentFloor;
    }

    public List<People> generate(int min, int max) {
        //0 <= n <= 10
        List<People> res = new ArrayList<>();
        IntStream.range(min, this.random(min, max)).forEach(x -> res.add(new People(this.random(0, floors, currentFloor))));
        return res;
    }
}
