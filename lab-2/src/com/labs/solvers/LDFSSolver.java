package com.labs.solvers;

import com.labs.utils.GameNode;
import com.labs.utils.QueenPosition;
import com.labs.utils.SearchResult;

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
    private static final Integer QUEENS = 8;
    private static final Integer BOARD = 8;

    private Long time = 0L;
    private Integer iterations = 0;
    private Integer fails = 0;
    private Integer states = 0;


    public LDFSSolver(Integer MAX_DEPTH) {
        this.MAX_DEPTH = MAX_DEPTH;
        this.parentNode = new GameNode(generateInitialPlacement(QUEENS), 0);
        this.checkedStates = new ArrayList<>();
        printSolution("START POSITION", parentNode.getPositions(), BOARD);
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
        if (validatePositions(currentNode.getPositions())) {
            solution = currentNode;
            return new SearchResult(currentNode, true);
        }
        if (Objects.equals(currentNode.getDepth(), MAX_DEPTH)) {
            return new SearchResult(CUTOFF_MESSAGE, false);
        }
        List<GameNode> successors = generateSuccessors(currentNode);
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

    private List<GameNode> generateSuccessors(GameNode currentState) {
        List<GameNode> successors = new ArrayList<>();
        checkedStates.add(currentState.getPositions());

        for (int currentCol = 0; currentCol < QUEENS; currentCol++) {
            QueenPosition takenPositions = getCurrentPosition(currentState.getPositions(), currentCol);
            if (takenPositions != null) {
                List<QueenPosition> otherQueens = retainAllNotEmptyPositions(currentState.getPositions(), currentCol);
                List<Integer> rows = IntStream.rangeClosed(0, 7).boxed().collect(Collectors.toList());
                rows.remove(takenPositions.getyPos());

                for (Integer row : rows) {
                    List<QueenPosition> positions = new ArrayList<>(otherQueens);
                    positions.add(new QueenPosition(currentCol, row));
                    if (!isStateChecked(positions)) {
                        successors.add(new GameNode(positions, currentState.getDepth() + 1));
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


    public void printReport(SearchResult result) {
        System.out.println();
        String pattern = "| %-13s | %-8s |%n";
        String headerPattern = "| %-13s | %-8s |%n";
        if (result.isSuccess()) {
            printSolution("SOLUTION", result.getSolution().getPositions(), BOARD);
        } else {
            System.out.printf(headerPattern, "METRICS", "ERROR");
            System.out.printf("---------------------------%n");
            System.out.printf(pattern, "STATUS", result.getMessage());
        }

        System.out.printf(pattern, "ITERATIONS", iterations.toString());
        System.out.printf(pattern, "FAILS", fails.toString());
        System.out.printf(pattern, "STATES", states.toString());
        System.out.printf(pattern, "MEMORY STATES", checkedStates.size());
        System.out.printf(pattern, "TIME", time.toString() + " ms");
    }

}
