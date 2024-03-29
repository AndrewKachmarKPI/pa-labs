package com.labs.utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import static com.labs.utils.GameUtils.isConflict;

public class GameNode {
    private List<QueenPosition> positions;
    private Integer depth;
    private Integer functionCost;

    public GameNode(List<QueenPosition> positions, Integer depth, Boolean isCountHeuristic) {
        if (depth < 0) {
            throw new RuntimeException("Game node params are not valid");
        }
        this.positions = positions;
        this.depth = depth;
        if (isCountHeuristic) {
            this.functionCost = getHeuristicCost();
        }
    }

    public GameNode(List<QueenPosition> positions) {
        this.positions = positions;
    }

    public List<QueenPosition> getPositions() {
        return positions;
    }

    public Integer getDepth() {
        return depth;
    }

    public Integer getTotalCost() {
        return this.depth + this.functionCost;
    }

    public Integer getHeuristicCost() {
        Map<QueenPosition, QueenPosition> positionMap = new HashMap<>();
        this.positions.forEach(pos1 -> this.positions.forEach(pos2 -> {
            boolean isDuplicate = positionMap.containsKey(pos1) && positionMap.get(pos1).equals(pos2) ||
                    positionMap.containsKey(pos2) && positionMap.get(pos2).equals(pos1);
            if (isConflict(this.positions, pos1, pos2) && !pos1.equals(pos2) && !isDuplicate) {
                positionMap.put(pos1, pos2);
            }
        }));
        return positionMap.size();
    }
}
