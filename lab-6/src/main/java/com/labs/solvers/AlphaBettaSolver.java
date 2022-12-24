package com.labs.solvers;

import com.labs.domain.BoxBorder;
import com.labs.domain.GameBoardNode;
import com.labs.domain.GameBox;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.*;

public class AlphaBettaSolver implements GameSolver {
    private static Integer alpha = Integer.MIN_VALUE; // BEST FOR MAX player
    private static Integer beta = Integer.MAX_VALUE;  // BEST FOR MIN player
    private GameBoardNode rootState;

    public AlphaBettaSolver(GameBoardNode rootState) {
        this.rootState = rootState;
    }

    // If alpha >= beta then prune
    // If alpha < beta DON'T prune
//    private String getNextMove(GameBoard board, GameDifficulty difficulty) {
//
//    }
//
    public void alphaBettaMiniMax() {
        buildGameTree(rootState, "MAX");
        System.out.println("TEST");
    }

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
        Callable<GameBoardNode> task = new GenerateSuccessorTask(generateBy, currentState,false);
        return executorService.submit(task).get().getSuccessors();
    }

//    //SUCCESSORS
}
