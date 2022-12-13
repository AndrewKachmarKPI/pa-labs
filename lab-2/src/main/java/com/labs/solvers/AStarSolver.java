package com.labs.solvers;

import com.labs.utils.*;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

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
            if (currentNode != null && isPositionsValid(currentNode.getPositions())) {
                time = System.currentTimeMillis() - start;
                return new SearchResult(currentNode, true);
            }
            closedNodesQueue.add(currentNode);
            memStates = closedNodesQueue.size();
            List<GameNode> gameNodes = getGameNodes(currentNode);
            fillOpenedQueue(openNodesQueue, closedNodesQueue, gameNodes);
        }
        time = System.currentTimeMillis() - start;
        return new SearchResult("failure", false);
    }

    private void fillOpenedQueue(PriorityQueue<GameNode> openNodesQueue, PriorityQueue<GameNode> closedNodesQueue, List<GameNode> gameNodes) {
        for (GameNode gameNode : gameNodes) {
            if (!closedNodesQueue.contains(gameNode)) {
                openNodesQueue.add(gameNode);
            } else {
                fails++;
            }
        }
    }

    private List<GameNode> getGameNodes(GameNode currentNode) {
        closedGameNodes.add(currentNode);
        List<GameNode> gameNodes = getGeneratedGameNodes(currentNode, closedGameNodes, true);
        states += gameNodes.size();
        return gameNodes;
    }

    public SearchPositionMetrics getSearchPositionMetrics() {
        return new SearchPositionMetrics(time, iterations, fails, states, memStates);
    }
}
