package com.labs.solvers;

import com.labs.utils.GameNode;
import com.labs.utils.GameNodeComparator;
import com.labs.utils.QueenPosition;
import com.labs.utils.SearchResult;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.labs.utils.GameUtils.*;

public class AStarSolver {
    private List<GameNode> closed = new ArrayList<>();
    private GameNode parentNode;

    private static final Integer QUEENS = 8;
    private static final Integer BOARD = 8;

    private Long time = 0L;
    private Integer iterations = 0;
    private Integer fails = 0;
    private Integer states = 0;
    private Integer memStates = 0;


    public AStarSolver() {
        this.parentNode = new GameNode(generateInitialPlacement(QUEENS), 0, true);
        printSolution("START POSITION", parentNode.getPositions(), BOARD);
    }


    public SearchResult aStarSearch() {
        long start = System.currentTimeMillis();
        PriorityQueue<GameNode> open = new PriorityQueue<>(new GameNodeComparator());
        PriorityQueue<GameNode> closed = new PriorityQueue<>(new GameNodeComparator());
        open.add(parentNode);
        while (!open.isEmpty()) {
            iterations++;
            GameNode currentNode = open.poll();
            if (validatePositions(currentNode.getPositions())) {
                time = System.currentTimeMillis() - start;
                return new SearchResult(currentNode, true);
            }
            closed.add(currentNode);
            memStates = closed.size();
            List<GameNode> successors = generateSuccessors(currentNode);
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


    private List<GameNode> generateSuccessors(GameNode currentState) {
        List<GameNode> successors = new ArrayList<>();
        closed.add(currentState);

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
        System.out.printf(pattern, "MEMORY STATES", memStates.toString());
        System.out.printf(pattern, "TIME", time.toString() + " ms");
    }

}
