package com.labs.solvers;

import com.labs.utils.*;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.labs.utils.GameUtils.*;

public class AStarSolver {
    private List<GameNode> closed = new ArrayList<>();
    private GameNode parentNode;

    private Long time = 0L;
    private Integer iterations = 0;
    private Integer fails = 0;
    private Integer states = 0;
    private Integer memStates = 0;

    public AStarSolver() {
        this.parentNode = new GameNode(getInitialPlacement(QUEENS), 0, true);
        printResultPosition("START POSITION", parentNode.getPositions());
    }

    public AStarSolver(List<QueenPosition> placement) {
        this.parentNode = new GameNode(placement, 0, true);
        printResultPosition("START POSITION", parentNode.getPositions());
    }

    public SearchResult aStarSearch() {
        long start = System.currentTimeMillis();
        PriorityQueue<GameNode> open = new PriorityQueue<>(new GameNodeComparator());
        PriorityQueue<GameNode> closed = new PriorityQueue<>(new GameNodeComparator());
        open.add(parentNode);
        while (!open.isEmpty() && !parentNode.getPositions().isEmpty()) {
            iterations++;
            GameNode currentNode = open.poll();
            if (validatePositions(currentNode.getPositions())) {
                time = System.currentTimeMillis() - start;
                return new SearchResult(currentNode, true);
            }
            closed.add(currentNode);
            memStates = closed.size();
            List<GameNode> successors = getSuccessors(currentNode);
            states += successors.size();
            for (GameNode successor : successors) {
                if (!closed.contains(successor)) {
                    open.add(successor);
                } else {
                    fails++;
                }
            }
        }
        time = System.currentTimeMillis() - start;
        return new SearchResult("failure", false);
    }

    private List<GameNode> getSuccessors(GameNode currentState) {
        List<GameNode> successors = new ArrayList<>();
        closed.add(currentState);

        for (int currentCol = 0; currentCol < QUEENS; currentCol++) {
            QueenPosition takenPositions = getCurrentPosition(currentState.getPositions(), currentCol);
            if (takenPositions != null) {
                List<QueenPosition> otherQueens = getAllNotEmptyPositions(currentState.getPositions(), currentCol);
                List<Integer> rows = IntStream.rangeClosed(0, 7).boxed().collect(Collectors.toList());
                rows.remove(takenPositions.getyPos());

                for (Integer row : rows) {
                    List<QueenPosition> positions = new ArrayList<>(otherQueens);
                    positions.add(new QueenPosition(currentCol, row));
                    if (!isStateChecked(positions)) {
                        successors.add(new GameNode(positions, currentState.getDepth() + 1, true));
                    }
                }
            }
        }
        return successors;
    }

    private boolean isStateChecked(List<QueenPosition> currentState) {
        return closed.stream().anyMatch(state -> state.getPositions().equals(currentState));
    }

    public SearchPositionMetrics getSearchPositionMetrics() {
        return new SearchPositionMetrics(time, iterations, fails, states, memStates);
    }
}
