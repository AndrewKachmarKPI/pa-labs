package com.labs.solvers;

import com.labs.utils.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.labs.utils.GameUtils.*;

public class LDFSSolver {
    private GameNode solutionGameNode;
    private GameNode parentGameNode;
    private List<GameNode> checkedQueenPositions;

    private Integer MAX_DEPTH;
    private static final String CUTOFF_MESSAGE = "cutoff";

    private Long time = 0L;
    private Integer iterations = 0;
    private Integer fails = 0;
    private Integer states = 0;

    public LDFSSolver(Integer MAX_DEPTH) {
        this.MAX_DEPTH = MAX_DEPTH;
        this.parentGameNode = new GameNode(getInitialPlacement(QUEENS), 0, false);
        this.checkedQueenPositions = new ArrayList<>();
        printResultPosition("START POSITION", parentGameNode.getPositions());
    }

    public LDFSSolver(Integer MAX_DEPTH, List<QueenPosition> placement) {
        this.MAX_DEPTH = MAX_DEPTH;
        this.parentGameNode = new GameNode(placement, 0, false);
        this.checkedQueenPositions = new ArrayList<>();
        printResultPosition("START POSITION", parentGameNode.getPositions());
    }

    public SearchResult depthLimitedSearch() {
        time = System.currentTimeMillis();
        SearchResult result = recursiveDls(parentGameNode);
        time = System.currentTimeMillis() - time;
        return result;
    }

    private SearchResult recursiveDls(GameNode currentNode) {
        iterations++;
        boolean isCutoff = false;
        if (isPositionsValid(currentNode.getPositions()) && !currentNode.getPositions().isEmpty()) {
            solutionGameNode = currentNode;
            return new SearchResult(currentNode, true);
        }
        if (Objects.equals(currentNode.getDepth(), MAX_DEPTH)) {
            return new SearchResult(CUTOFF_MESSAGE, false);
        }
        List<GameNode> gameNodes = getGameNodes(currentNode);
        for (GameNode gameNode : gameNodes) {
            SearchResult result = recursiveDls(gameNode);
            if (!result.isSuccess() && result.getMessage().equals(CUTOFF_MESSAGE)) {
                isCutoff = true;
            }
            if (result.isSuccess()) {
                result.setSolution(solutionGameNode);
                return result;
            }
        }
        return new SearchResult(isCutoff ? CUTOFF_MESSAGE : "failure", false);
    }

    private List<GameNode> getGameNodes(GameNode currentState) {
        checkedQueenPositions.add(new GameNode(currentState.getPositions()));
        List<GameNode> gameNodes = getGeneratedGameNodes(currentState, checkedQueenPositions,false);
        states += gameNodes.size();
        if (gameNodes.isEmpty()) {
            fails++;
        }
        return gameNodes;
    }

    public SearchPositionMetrics getSearchPositionMetrics() {
        return new SearchPositionMetrics(time, iterations, fails, states);
    }
}
