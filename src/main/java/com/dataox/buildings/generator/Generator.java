package com.dataox.buildings.generator;

import java.util.Collection;

public interface Generator<T> {
    Collection<T> generate(int bottomLimit, int topLimit);

    default int random(int min, int max) {
        max -= min;
        return (int) (Math.random() * ++max) + min;
    }

    default int random(int min, int max, int exc) {
        max -= min;
        int a;
        while(true){
            a = (int) (Math.random() * max) + min;
            if (a != exc){
                break;
            }
        }
        return a;
    }
}
