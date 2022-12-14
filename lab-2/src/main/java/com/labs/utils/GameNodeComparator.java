package com.labs.utils;

import java.util.Comparator;

public class GameNodeComparator implements Comparator<GameNode> {
    @Override
    public int compare(GameNode o1, GameNode o2) {
        return o1.getTotalCost().compareTo(o2.getTotalCost());
    }
}
