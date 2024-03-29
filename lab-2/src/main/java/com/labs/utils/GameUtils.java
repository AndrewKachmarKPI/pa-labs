package com.labs.utils;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class GameUtils {
    public static final Integer QUEENS = 8;
    public static final Integer BOARD = 8;

    public static boolean isPositionsValid(List<QueenPosition> positions) {
        return getConflictsMap(positions).isEmpty();
    }

    private static Map<QueenPosition, QueenPosition> getConflictsMap(List<QueenPosition> positions) {
        Map<QueenPosition, QueenPosition> conflictsMap = new HashMap<>();
        positions.forEach(pos1 -> positions.forEach(pos2 -> {
            if (isConflict(pos1, pos2) && !pos1.equals(pos2)) {
                conflictsMap.put(pos1, pos2);
            }
        }));
        return conflictsMap;
    }

    public static boolean isConflict(QueenPosition pos1, QueenPosition pos2) {
        return Objects.equals(pos1.getxPos(), pos2.getxPos()) || Objects.equals(pos1.getyPos(), pos2.getyPos()) ||
                Math.abs(pos1.getxPos() - pos2.getxPos()) == Math.abs(pos1.getyPos() - pos2.getyPos());
    }

    public static boolean isConflict(List<QueenPosition> positions, QueenPosition pos1, QueenPosition pos2) {
        return isConflict(pos1, pos2) && isAnyPositionBetween(positions, pos1, pos2);
    }

    private static boolean isAnyPositionBetween(List<QueenPosition> positions, QueenPosition pos1, QueenPosition pos2) {
        List<Integer> xRange = IntStream.rangeClosed(pos1.getxPos(), pos2.getxPos()).boxed().collect(Collectors.toList());
        List<Integer> yRange = IntStream.rangeClosed(pos1.getyPos(), pos2.getyPos()).boxed().collect(Collectors.toList());
        return positions.stream()
                .filter(queenPosition -> queenPosition.equals(pos1) && queenPosition.equals(pos2))
                .noneMatch(queenPosition -> yRange.contains(queenPosition.getyPos()) && xRange.contains(queenPosition.getxPos()));
    }

    public static QueenPosition getCurrentPosition(List<QueenPosition> positions, Integer x) {
        return positions.stream().filter(queenPosition -> queenPosition.getxPos().equals(x))
                .findAny().orElse(null);
    }

    public static List<QueenPosition> getInitialPlacement(int queensSize) {
        List<QueenPosition> positions = new ArrayList<>();
        List<Integer> cols = IntStream.rangeClosed(0, 7).boxed().collect(Collectors.toList());
        for (Integer col : cols) {
            QueenPosition position = new QueenPosition(col, getRandomNumber(0, queensSize));
            while (positions.contains(position)) {
                position = getRandomPosition(queensSize);
            }
            positions.add(position);
        }
        return positions;
    }

    public static List<QueenPosition> getAllNotEmptyPositions(List<QueenPosition> positions, Integer except) {
        List<QueenPosition> positionList = new ArrayList<>();
        List<Integer> cols = IntStream.rangeClosed(0, 7).boxed().collect(Collectors.toList());
        cols.remove(except);
        for (Integer col : cols) {
            positionList.add(getCurrentPosition(positions, col));
        }
        return positionList;
    }

    public static void printResultPosition(String title, List<QueenPosition> positions) {
        String pattern = "| %-1s | %-8s |%n";
        System.out.println(title);
        for (int i = 0; i < BOARD; i++) {
            StringBuilder line = new StringBuilder();
            for (int j = 0; j < BOARD; j++) {
                int finalJ = j;
                int finalI = i;
                boolean isMatch = positions.stream().anyMatch(pos -> pos.getxPos().equals(finalJ) && pos.getyPos().equals(finalI));
                line.append(isMatch ? " Q " : " * ");
            }
            System.out.printf(pattern, i + 1, line);
        }
    }

    public static QueenPosition getRandomPosition(int queensSize) {
        return new QueenPosition(getRandomNumber(0, queensSize), getRandomNumber(0, queensSize));
    }

    private static int getRandomNumber(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
    }

    public static int getEnterDepth() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter LDFS depth limit:");
        int depth = scanner.nextInt();
        if (depth <= 0) {
            System.out.println("Invalid depth");
            return getEnterDepth();
        }
        return depth;
    }

    public static void printSearchResultPositionAndMetrics(SearchResult result, SearchPositionMetrics metrics) {
        System.out.println();
        String pattern = "| %-13s | %-8s |%n";
        String headerPattern = "| %-13s | %-8s |%n";
        if (result.isSuccess()) {
            printResultPosition("SOLUTION", result.getSolution().getPositions());
        } else {
            System.out.printf(headerPattern, "METRICS", "ERROR");
            System.out.printf("---------------------------%n");
            System.out.printf(pattern, "STATUS", result.getMessage());
        }

        System.out.printf(pattern, "ITERATIONS", metrics.getIterations().toString());
        System.out.printf(pattern, "FAILS", metrics.getFails().toString());
        System.out.printf(pattern, "STATES", metrics.getStates().toString());
        System.out.printf(pattern, "MEMORY STATES", metrics.getMemStates().toString());
        System.out.printf(pattern, "TIME", metrics.getTime().toString() + " ms");
    }

    public static List<GameNode> getGeneratedGameNodes(GameNode currentState, List<GameNode> checkedQueenPositions, boolean isCountHeuristic) {
        List<GameNode> gameNodes = new ArrayList<>();
        for (int currentCol = 0; currentCol < QUEENS; currentCol++) {
            QueenPosition takenPositions = getCurrentPosition(currentState.getPositions(), currentCol);
            if (takenPositions != null) {
                List<QueenPosition> otherQueens = getAllNotEmptyPositions(currentState.getPositions(), currentCol);
                List<Integer> rows = getEmptyRows(takenPositions);
                GenerateGameNodeParams params = new GenerateGameNodeParams(rows, otherQueens, currentState, currentCol, isCountHeuristic);
                List<GameNode> filledGameNodes = getFilledGameNodes(params, checkedQueenPositions);
                gameNodes.addAll(filledGameNodes);
            }
        }
        return gameNodes;
    }

    private static List<GameNode> getFilledGameNodes(GenerateGameNodeParams params, List<GameNode> checkedQueenPositions) {
        List<GameNode> gameNodes = new ArrayList<>();
        for (Integer row : params.getRows()) {
            List<QueenPosition> positions = new ArrayList<>(params.getQueenPositions());
            positions.add(new QueenPosition(params.getCurrentColumn(), row));
            if (!isStateChecked(positions, checkedQueenPositions)) {
                gameNodes.add(new GameNode(positions, params.getCurrentGameNode().getDepth() + 1, params.isCountHeuristic()));
            }
        }
        return gameNodes;
    }

    public static List<Integer> getEmptyRows(QueenPosition takenPosition) {
        List<Integer> rows = IntStream.rangeClosed(0, 7).boxed().collect(Collectors.toList());
        rows.remove(takenPosition.getyPos());
        return rows;
    }

    private static boolean isStateChecked(List<QueenPosition> queenPositions, List<GameNode> checkedQueenPositions) {
        return checkedQueenPositions.stream().anyMatch(position -> position.getPositions().equals(queenPositions));
    }
}
