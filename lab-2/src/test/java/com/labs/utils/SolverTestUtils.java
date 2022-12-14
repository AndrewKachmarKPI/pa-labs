package com.labs.utils;

import java.util.ArrayList;
import java.util.List;

import static com.labs.utils.GameUtils.QUEENS;
import static com.labs.utils.GameUtils.getInitialPlacement;

public class SolverTestUtils {
    public static List<QueenPosition> getRandomPlacement() {
        return getInitialPlacement(QUEENS);
    }
    public static List<QueenPosition> getCorrectPlacement() {
        List<QueenPosition> queenPositions = new ArrayList<>();
        queenPositions.add(new QueenPosition(2, 0));
        queenPositions.add(new QueenPosition(5, 1));
        queenPositions.add(new QueenPosition(7, 2));
        queenPositions.add(new QueenPosition(0, 3));
        queenPositions.add(new QueenPosition(3, 4));
        queenPositions.add(new QueenPosition(6, 5));
        queenPositions.add(new QueenPosition(4, 6));
        queenPositions.add(new QueenPosition(1, 7));
        return queenPositions;
    }
}
