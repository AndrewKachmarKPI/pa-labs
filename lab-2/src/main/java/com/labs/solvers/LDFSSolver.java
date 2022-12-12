package com.labs.solvers;

import com.labs.utils.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.labs.utils.GameUtils.*;

public class LDFSSolver {
    private GameNode solution;
    private GameNode parentNode;
    private List<List<QueenPosition>> checkedStates;

    private Integer MAX_DEPTH;
    private static final String CUTOFF_MESSAGE = "cutoff";

    private Long time = 0L;
    private Integer iterations = 0;
    private Integer fails = 0;
    private Integer states = 0;

    public LDFSSolver(Integer MAX_DEPTH) {
        this.MAX_DEPTH = MAX_DEPTH;
        this.parentNode = new GameNode(getInitialPlacement(QUEENS), 0,false);
        this.checkedStates = new ArrayList<>();
        printResultPosition("START POSITION", parentNode.getPositions());
    }

    public LDFSSolver(Integer MAX_DEPTH, List<QueenPosition> placement) {
        this.MAX_DEPTH = MAX_DEPTH;
        this.parentNode = new GameNode(placement, 0,false);
        this.checkedStates = new ArrayList<>();
        printResultPosition("START POSITION", parentNode.getPositions());
    }

    public SearchResult depthLimitedSearch() {
        time = System.currentTimeMillis();
        SearchResult result = recursiveDls(parentNode);
        time = System.currentTimeMillis() - time;
        return result;
    }

    private SearchResult recursiveDls(GameNode currentNode) {
        iterations++;
        boolean isCutoff = false;
        if (validatePositions(currentNode.getPositions()) && !currentNode.getPositions().isEmpty()) {
            solution = currentNode;
            return new SearchResult(currentNode, true);
        }
        if (Objects.equals(currentNode.getDepth(), MAX_DEPTH)) {
            return new SearchResult(CUTOFF_MESSAGE, false);
        }
        List<GameNode> successors = getSuccessors(currentNode);
        states += successors.size();
        for (GameNode successor : successors) {
            SearchResult result = recursiveDls(successor);
            if (!result.isSuccess() && result.getMessage().equals(CUTOFF_MESSAGE)) {
                isCutoff = true;
            }
            if (result.isSuccess()) {
                result.setSolution(solution);
                return result;
            }
        }
        return new SearchResult(isCutoff ? CUTOFF_MESSAGE : "failure", false);
    }

    private List<GameNode> getSuccessors(GameNode currentState) {
        List<GameNode> successors = new ArrayList<>();
        checkedStates.add(currentState.getPositions());

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
                        successors.add(new GameNode(positions, currentState.getDepth() + 1, false));
                    }
                }
            }
        }
        if (successors.isEmpty()) {
            fails++;
        }
        return successors;
    }

    private boolean isStateChecked(List<QueenPosition> currentState) {
        return checkedStates.stream().anyMatch(positions -> positions.equals(currentState));
    }

    public SearchPositionMetrics getSearchPositionMetrics() {
        return new SearchPositionMetrics(time, iterations, fails, states);
    }
}
