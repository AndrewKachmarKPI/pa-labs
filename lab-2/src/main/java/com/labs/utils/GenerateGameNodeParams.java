package com.labs.utils;

import java.util.List;

public class GenerateGameNodeParams {
    private List<Integer> rows;
    private List<QueenPosition> queenPositions;
    private GameNode currentGameNode;
    private int currentColumn;
    private boolean isCountHeuristic;

    public GenerateGameNodeParams(List<Integer> rows, List<QueenPosition> queenPositions, GameNode currentGameNode, int currentColumn, boolean isCountHeuristic) {
        this.rows = rows;
        this.queenPositions = queenPositions;
        this.currentGameNode = currentGameNode;
        this.currentColumn = currentColumn;
        this.isCountHeuristic = isCountHeuristic;
    }

    public List<Integer> getRows() {
        return rows;
    }

    public boolean isCountHeuristic() {
        return isCountHeuristic;
    }

    public List<QueenPosition> getQueenPositions() {
        return queenPositions;
    }

    public GameNode getCurrentGameNode() {
        return currentGameNode;
    }

    public int getCurrentColumn() {
        return currentColumn;
    }
}
