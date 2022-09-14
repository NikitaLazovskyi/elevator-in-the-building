package com.dataox.buildings.generator.impl;

import com.dataox.buildings.generator.Generator;
import com.dataox.buildings.models.Floor;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

@Data
public class GeneratorFloor implements Generator<Floor> {

    //0 <= x < 10
    private int maxPeoples = 10;
    private int minPeoples = 0;

    @Override
    public List<Floor> generate(int min, int max) {
        int floors = this.random(min, max);
        List<Floor> res = new ArrayList<>();
        IntStream.range(0, floors).forEach(x -> res.add(new Floor(new GeneratorPeople(floors, x).generate(minPeoples, maxPeoples), x)));
        return res;
    }
}
