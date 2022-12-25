package com.labs.solvers;

import com.labs.domain.BoxBorder;
import com.labs.domain.GameBoardNode;
import com.labs.domain.GameBox;
import com.labs.enums.GameDifficulty;
import com.labs.enums.PlayerType;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.*;
import java.util.stream.Collectors;


// If alpha >= beta then prune
// If alpha < beta DON'T prune
public class AlphaBettaSolver implements GameSolver {
    private GameBoardNode rootState;
    private GameDifficulty difficulty;

    public AlphaBettaSolver(GameBoardNode rootState, GameDifficulty difficulty) {
        this.rootState = rootState;
        this.difficulty = difficulty;
    }

    public String getNextMove() throws ExecutionException, InterruptedException {
        int alpha = Integer.MIN_VALUE;
        int beta = Integer.MAX_VALUE;
        GameBoardNode bestNode = alphaBetaSearch(rootState, difficulty.getDifficulty(), alpha, beta);
        BoxBorder border = getBestBoxBorder(rootState, bestNode);

        rootState = new GameBoardNode(bestNode);

        System.out.println("NODE->" + bestNode);
        System.out.println("BEST BORDER->" + border);
        return border.getId();
    }

    public GameBoardNode alphaBetaSearch(GameBoardNode currentBoard, Integer difficulty, int alpha, int beta) throws ExecutionException, InterruptedException {
        generateChild(currentBoard, currentBoard.getMoveBy().toString());
        if (currentBoard.getDepth() >= difficulty || currentBoard.isLeaf()) {
            currentBoard.setFunctionCost(currentBoard.getHumanScore() + currentBoard.getComputerScore());
            return currentBoard;
        }
        GameBoardNode bestNode = new GameBoardNode();
        if (currentBoard.getMoveBy() == PlayerType.COMPUTER) {
            bestNode.setFunctionCost(Integer.MIN_VALUE);

            GameBoardNode boardNode;
            for (GameBoardNode successor : currentBoard.getSuccessors()) {
                boardNode = alphaBetaSearch(successor, difficulty - 1, alpha, beta);
                if (boardNode.getFunctionCost() > bestNode.getFunctionCost())
                    bestNode = boardNode;
                if (bestNode.getFunctionCost() > alpha)
                    alpha = bestNode.getFunctionCost();
                if (beta <= alpha)
                    break;
            }
        } else {
            bestNode.setFunctionCost(Integer.MAX_VALUE);
            GameBoardNode boardNode;
            for (GameBoardNode successor : currentBoard.getSuccessors()) {
                boardNode = alphaBetaSearch(successor, difficulty - 1, alpha, beta);
                if (boardNode.getFunctionCost() < bestNode.getFunctionCost())
                    bestNode = boardNode;
                if (bestNode.getFunctionCost() < beta)
                    beta = bestNode.getFunctionCost();
                if (beta <= alpha)
                    break;
            }
        }
        currentBoard.setFunctionCost(bestNode.getFunctionCost());
        return bestNode;
    }

    private void generateChild(GameBoardNode currentBoard, String generateBy) throws ExecutionException, InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(1);
        Callable<GameBoardNode> gameBoardNodeCallable = new GenerateSuccessorTask(generateBy, currentBoard, true);
        executorService.submit(gameBoardNodeCallable).get();
    }


    //BUILD TREE
    public void buildGameTree(GameBoardNode currentState, String generateBy) {
        try {
            ExecutorService executorService = Executors.newFixedThreadPool(24);
            List<Callable<GameBoardNode>> callables = new ArrayList<>();
            List<GameBoardNode> successors = getFirstSuccessors(currentState, generateBy);

            for (GameBoardNode successor : successors) {
                callables.add(new GenerateSuccessorTask(generateBy, successor, true));
            }
            successors.clear();

            List<Future<GameBoardNode>> futures = executorService.invokeAll(callables);
            for (Future<GameBoardNode> future : futures) {
                successors.add(future.get());
            }

            currentState.setSuccessors(successors);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private List<GameBoardNode> getFirstSuccessors(GameBoardNode currentState, String generateBy) throws Exception {
        ExecutorService executorService = Executors.newFixedThreadPool(1);
        Callable<GameBoardNode> task = new GenerateSuccessorTask(generateBy, currentState, false);
        return executorService.submit(task).get().getSuccessors();
    }

    private BoxBorder getBestBoxBorder(GameBoardNode currentState, GameBoardNode boardNode) {
        List<BoxBorder> currentStateBoxBorders = currentState.getDistinctAllBoxBorders();

        List<BoxBorder> newBoxBorders = boardNode.getDistinctAllBoxBorders();


        BoxBorder newBorder = null;
        for (BoxBorder currentBorder : currentStateBoxBorders) {
            Optional<BoxBorder> boxBorder = newBoxBorders.stream()
                    .filter(border -> border.getId().equals(currentBorder.getId()) && currentBorder.isSelected() != border.isSelected())
                    .findAny();
            if (boxBorder.isPresent()) {
                newBorder = boxBorder.get();
                break;
            }
        }
        return newBorder;
    }
}
