package com.labs.solvers;

import com.labs.utils.*;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.labs.utils.GameUtils.*;

public class AStarSolver {
    private List<GameNode> closedGameNodes = new ArrayList<>();
    private GameNode parentGameNode;

    private Long time = 0L;
    private Integer iterations = 0;
    private Integer fails = 0;
    private Integer states = 0;
    private Integer memStates = 0;

    public AStarSolver() {
        this.parentGameNode = new GameNode(getInitialPlacement(QUEENS), 0, true);
        printResultPosition("START POSITION", parentGameNode.getPositions());
    }

    public AStarSolver(List<QueenPosition> placement) {
        this.parentGameNode = new GameNode(placement, 0, true);
        printResultPosition("START POSITION", parentGameNode.getPositions());
    }

    public SearchResult aStarSearch() {
        long start = System.currentTimeMillis();
        PriorityQueue<GameNode> openNodesQueue = new PriorityQueue<>(new GameNodeComparator());
        PriorityQueue<GameNode> closedNodesQueue = new PriorityQueue<>(new GameNodeComparator());
        openNodesQueue.add(parentGameNode);
        while (!openNodesQueue.isEmpty() && !parentGameNode.getPositions().isEmpty()) {
            iterations++;
            GameNode currentNode = openNodesQueue.poll();
            if (isPositionsValid(currentNode.getPositions())) {
                time = System.currentTimeMillis() - start;
                return new SearchResult(currentNode, true);
            }
            closedNodesQueue.add(currentNode);
            memStates = closedNodesQueue.size();
            List<GameNode> gameNodes = getGameNodes(currentNode);
            states += gameNodes.size();
            for (GameNode gameNode : gameNodes) {
                if (!closedNodesQueue.contains(gameNode)) {
                    openNodesQueue.add(gameNode);
                } else {
                    fails++;
                }
            }
        }
        time = System.currentTimeMillis() - start;
        return new SearchResult("failure", false);
    }

    private List<GameNode> getGameNodes(GameNode currentNode) {
        List<GameNode> gameNodes = new ArrayList<>();
        closedGameNodes.add(currentNode);

        for (int currentCol = 0; currentCol < QUEENS; currentCol++) {
            QueenPosition takenQueenPosition = getCurrentPosition(currentNode.getPositions(), currentCol);
            if (takenQueenPosition != null) {
                List<QueenPosition> otherQueenPositions = getAllNotEmptyPositions(currentNode.getPositions(), currentCol);
                List<Integer> rows = IntStream.rangeClosed(0, 7).boxed().collect(Collectors.toList());
                rows.remove(takenQueenPosition.getyPos());

                for (Integer row : rows) {
                    List<QueenPosition> positions = new ArrayList<>(otherQueenPositions);
                    positions.add(new QueenPosition(currentCol, row));
                    if (!isStateChecked(positions)) {
                        gameNodes.add(new GameNode(positions, currentNode.getDepth() + 1, true));
                    }
                }
            }
        }
        return gameNodes;
    }

    private boolean isStateChecked(List<QueenPosition> currentState) {
        return closedGameNodes.stream().anyMatch(state -> state.getPositions().equals(currentState));
    }

    public SearchPositionMetrics getSearchPositionMetrics() {
        return new SearchPositionMetrics(time, iterations, fails, states, memStates);
    }
}
